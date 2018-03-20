package dlt.study.netflix.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import dlt.study.log4j.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * 通过HystrixCollapser合并请求提高应用吞吐量
 */
public class CommandCollapserGetValueForKey extends HystrixCollapser<List<String>, String, Integer> {

    private final Integer key;

    public CommandCollapserGetValueForKey(Integer key) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("CommandCollapserGetValueForKey"))
                .andCollapserPropertiesDefaults(
                        HystrixCollapserProperties.Setter()
                                .withTimerDelayInMilliseconds(100)
                                .withMaxRequestsInBatch(100))
                .andScope(Scope.GLOBAL) // Scope.REQUEST

        );
        this.key = key;
    }

    @Override
    public Integer getRequestArgument() {
        return key;
    }

    @Override
    protected HystrixCommand<List<String>> createCommand(final Collection<CollapsedRequest<String, Integer>> requests) {
        List<Integer> requestIds = requests.stream().map(t -> t.getArgument()).collect(Collectors.toList());
        return new BatchCommand(requestIds);
    }

    @Override
    protected void mapResponseToRequests(List<String> batchResponse, Collection<CollapsedRequest<String, Integer>> requests) {
        int count = 0;
        for (CollapsedRequest<String, Integer> request : requests) {  // 注意：在真实的业务中顺序可能是乱序的
            request.setResponse(batchResponse.get(count++));
        }
    }

    private static final class BatchCommand extends HystrixCommand<List<String>> {
        private final List<Integer> requestIds;

        private BatchCommand(List<Integer> requestIds) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BatchCommand"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueForKey")));
            this.requestIds = requestIds;
        }

        @Override
        protected List<String> run() {
            Log.info("BatchCommand -> run  on " + requestIds.size());
            ArrayList<String> response = new ArrayList<>();
            for (Integer requestId : requestIds) {
                // artificial response for each argument received in the batch
                response.add("ValueForKey: " + requestId);
            }
            return response;
        }
    }

    public static void main(String[] args) throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            Future<String> f1 = new CommandCollapserGetValueForKey(1).queue();
            Future<String> f2 = new CommandCollapserGetValueForKey(2).queue();
            Future<String> f3 = new CommandCollapserGetValueForKey(3).queue();
            Future<String> f4 = new CommandCollapserGetValueForKey(4).queue();

            assertEquals("ValueForKey: 1", f1.get());
            assertEquals("ValueForKey: 2", f2.get());
            assertEquals("ValueForKey: 3", f3.get());
            assertEquals("ValueForKey: 4", f4.get());

            // assert that the batch command 'GetValueForKey' was in fact
            // executed and that it executed only once
            assertEquals(1, HystrixRequestLog.getCurrentRequest().getExecutedCommands().size());
            HystrixCommand<?> command = HystrixRequestLog.getCurrentRequest().getExecutedCommands().toArray(new HystrixCommand<?>[1])[0];
            // assert the command is the one we're expecting
            assertEquals("GetValueForKey", command.getCommandKey().name());
            // confirm that it was a COLLAPSED command execution
            assertTrue(command.getExecutionEvents().contains(HystrixEventType.COLLAPSED));
            // and that it was successful
            assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
        } finally {
            context.shutdown();
        }
    }
}