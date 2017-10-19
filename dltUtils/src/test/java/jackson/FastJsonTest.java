package jackson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by denglt on 2016/3/11.
 */
public class FastJsonTest {
    @Test
    public void parseObject() throws Exception {
        String content = "{\n" +
                "    \"result\":true,\n" +
                "    \"kind\":\"0\",\n" +
                "    \"msg\":\"成功\",\n" +
                "    \"data\":{\n" +
                "        \"referenceSection\":{\n" +
                "            \"yunSectId\":1010,\n" +
                "            \"yunSectName\":\"神经内科\",\n" +
                "            \"yunSectIconURL\":\"http://img.xxx.xxx/aaa.jpg\"\n" +
                "        },\n" +
                "        \"recommenedHospitals\":[{\n" +
                "            \"hosId\":99,\n" +
                "            \"name\":\"中山六院\",\n" +
                "            \"sectId\":456,\n" +
                "            \"sectName\":\"神经内科\",\n" +
                "            \"logo\":\"http://xxx.xxx.xxx.xxx/xxx.png\"\n" +
                "        },{\n" +
                "            \"hosId\":100,\n" +
                "            \"name\":\"华侨医院\",\n" +
                "            \"sectId\":678,\n" +
                "            \"sectName\":\"神经内科\",\n" +
                "            \"logo\":\"http://xxx.xxx.xxx.xxx/xxx.png\"\n" +
                "        }],\n" +
                "        \"nearbyHospitals\":[{\n" +
                "            \"hosId\":100,\n" +
                "            \"name\":\"华侨医院\",\n" +
                "            \"sectId\":678,\n" +
                "            \"logo\":\"http://xxx.xxx.xxx.xxx/xxx.png\",\n" +
                "            \"addr\":\"广州市天河区\",\n" +
                "            \"distance\":\"1.5KM\",\n" +
                "            \"longitude\":\"112.76654\",\n" +
                "            \"latitude\":\"23.87656\"\n" +
                "        },{\n" +
                "            \"hosId\":100,\n" +
                "            \"name\":\"中山六院\",\n" +
                "            \"sectId\":789,\n" +
                "            \"logo\":\"http://xxx.xxx.xxx.xxx/xxx.png\",\n" +
                "            \"addr\":\"广州市越秀区\",\n" +
                "            \"distance\":\"5KM\",\n" +
                "            \"longitude\":\"112.74654\",\n" +
                "            \"latitude\":\"23.87656\"\n" +
                "        }],\n" +
                "        \"recommenedDoctors\":[{\n" +
                "            \"docId\":9999,\n" +
                "            \"name\":\"张三\",\n" +
                "            \"mediLevelName\":\"主治医师\",\n" +
                "            \"hosName\":\"华侨医院\",\n" +
                "            \"regCount\":45,\n" +
                "            \"goodAt\":\"擅长XXXX\"\n" +
                "        },{\n" +
                "            \"docId\":8888,\n" +
                "            \"name\":\"李四\",\n" +
                "            \"mediLevelName\":\"住院医生\",\n" +
                "            \"hosName\":\"中山六院\",\n" +
                "            \"regCount\":34,\n" +
                "            \"goodAt\":\"擅长XXXX\"\n" +
                "        }],\n" +
                "        \"oltSection\":{\n" +
                "            \"yunSectId\":1010,\n" +
                "            \"yunSectName\":\"神经内科\",\n" +
                "            \"yunSectIconURL\":\"http://img.xxx.xxx/aaa.jpg\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        // ResultVo result  = JsonUtils.toObject(content,ResultVo.class); 错误
        // System.out.println(result);

        ResultVo result2 = JSON.parseObject(content, ResultVo.class);
        System.out.println(result2);

        Object object = JSON.parseObject(content);
        System.out.println(object.getClass());
        System.out.println(object);

        content = "{ kind : 'erere' }";

        ResultVo result = JSON.parseObject(content, ResultVo.class);
        System.out.println(result);

    }

    @Test
    public void parseNullStr() {
        ResultVo result = JSON.parseObject(null, ResultVo.class);
        System.out.println(result); //null
        result = JSON.parseObject("{}", ResultVo.class);
        System.out.println(result); //ResultVo{result=false, kind='null', msg='null', data='null'}
    }


    @Test
    public void parseArray() {
        List<ResultVo> vos = JSON.parseArray(null, ResultVo.class);
        System.out.println(vos);
        vos = JSON.parseArray("[]", ResultVo.class);
        System.out.println(vos.getClass());
    }

    @Test
    public void toJSON() {
        String json = JSON.toJSONString(new ArrayList<ResultVo>());
        System.out.println(json);//[]
        json = JSON.toJSONString(new ArrayList<ResultVo>(),SerializerFeature.WriteMapNullValue);
        System.out.println(json);//[]
        json = JSON.toJSONString(new ResultVo());
        System.out.println(json); //{"result":false}
        json = JSON.toJSONString(new ResultVo(),SerializerFeature.WriteMapNullValue);
        System.out.println(json); //{"data":null,"kind":null,"msg":null,"result":false}
        json = JSON.toJSONString(new ResultVo(),SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(json);//{"data":"","kind":"","msg":"","result":false}
    }

    @Test
    public void  toJSON2(){
        ResultVo v = new ResultVo();
        v.setData("22");
        v.setKind("11");
        String json = JSON.toJSONString(v,SerializerFeature.SortField);
        System.out.println(json);
        Object[] args = new Object[3];
        args[0] = v;
        args[1] = 1;
        args[2] = null;
        json = JSON.toJSONString(args,SerializerFeature.SortField);
        System.out.println(json);
    }

    @Test
    public void toJSONFromStr(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = baos.toByteArray();
        System.out.println(bytes.length);
        bytes = "{}".getBytes();
        Map map = JSON.parseObject(bytes, 0, bytes.length, Charset.forName("UTF-8").newDecoder(), Map.class);
        map.isEmpty();
        System.out.println(map.get("type"));
    }

    @Test
    public void copmlex(){
        /*ComplexResultVo complexResultVo = new ComplexResultVo();
        complexResultVo.setCheckResult(Lists.newArrayList(new ResultVo()));
        complexResultVo.setStudyResult(Lists.newArrayList(new ResultVo()));
        System.out.println(JSON.toJSONString(complexResultVo));*/
        String str = "{\"checkResult\":[{\"result\":true}],\"studyResult\":[{\"result\":true}]}";
        ComplexResultVo complexResultVo = JSON.parseObject(str, ComplexResultVo.class);
        System.out.println(complexResultVo);
        System.out.println(complexResultVo.getCheckResult().get(0).getClass());
        ResultVo resultVo = complexResultVo.getCheckResult().get(0);
        System.out.println(resultVo.isResult());
    }
}


class ComplexResultVo implements Serializable{
    private List<ResultVo> checkResult;
    private List<ResultVo> studyResult;

    public List<ResultVo> getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(List<ResultVo> checkResult) {
        this.checkResult = checkResult;
    }

    public List<ResultVo> getStudyResult() {
        return studyResult;
    }

    public void setStudyResult(List<ResultVo> studyResult) {
        this.studyResult = studyResult;
    }
}

class ResultVo implements Serializable {
    private boolean result = true;
    private String kind;
    private String msg;
    private String data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVo{" +
                "result=" + result +
                ", kind='" + kind + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
