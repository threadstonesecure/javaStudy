package dlt.study.kafka.streams;

import dlt.study.log4j.Log;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Optional;
import java.util.stream.Stream;

public class WordCountProcessor implements Processor<String, String> {

    private ProcessorContext context;
    private KeyValueStore<String, Integer> kvStore;

    @SuppressWarnings("unchecked")
    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.context.schedule(30000);// 设置调用punctuate 调用的时间间隔
        this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore("Counts");
    }

    @Override
    public void process(String key, String value) {
        Stream.of(value.toLowerCase().split(" ")).forEach((String word) -> {
            Optional<Integer> counts = Optional.ofNullable(kvStore.get(word));
            int count = counts.map(wordcount -> wordcount + 1).orElse(1);
            kvStore.put(word, count);
        });
    }

    @Override
    public void punctuate(long timestamp) {
        Log.info("WordCountProcessor.punctuate ->");
        KeyValueIterator<String, Integer> iterator = this.kvStore.all();
        iterator.forEachRemaining(entry -> {
           context.forward(entry.key, entry.value); // 给下个 downstream processors
           this.kvStore.delete(entry.key);
            Log.info(entry.key +" -> "+entry.value);
        });
        context.commit();
    }

    @Override
    public void close() {
        Log.info("WordCountProcessor.close ->");
        this.kvStore.close();
    }

}
