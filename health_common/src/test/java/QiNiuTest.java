import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiNiuTest {
    //使用七牛云提供的SDK实现将本地图片上传到七牛云服务器
    //@Test
    public void test1(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
//...其他参数参考类注释                       上传管理对象
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "WBWztd1YKOWtvsP2NY16e4963pKaf-5MxcmTW83W";
        String secretKey = "N_d8gdi_J0j5O3gnLwH0ypKPjMt75qcJC00SDnks";
        String bucket = "itehima";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "G:\\学习\\传智健康-加密\\传智健康\\day04\\资源\\图片资源\\03a36073-a140-4942-9b9b-712cecb144901.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "abc.jpg";
        //将用户名AK和密码SK传进去，创建认证对象
        Auth auth = Auth.create(accessKey, secretKey);
        //将存储空间名称bucket传进去鉴权，用来保证我提供的密钥是正确的，得到的upToken是这个令牌，
        // 我就可以在令牌正确的基础上进行文件上传了
        String upToken = auth.uploadToken(bucket);
        try {
            //返回响应数据
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的json格式的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //你指定上传文件的名称
            System.out.println(putRet.key);
            //上传文件的文件内容的hash值作为文件名
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //删除七牛云服务器中的图片
    //@Test
    public void test2(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        String accessKey = "WBWztd1YKOWtvsP2NY16e4963pKaf-5MxcmTW83W";
        String secretKey = "N_d8gdi_J0j5O3gnLwH0ypKPjMt75qcJC00SDnks";
        String bucket = "itehima";
        //指定要删除的文件名
        String key = "abc.jpg";
        //将用户名AK和密码SK传进去，创建认证对象
        Auth auth = Auth.create(accessKey, secretKey);
        //对用户名和密码进行鉴权，保证用户是本人操作
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //调用方法删除云端的文件
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}
