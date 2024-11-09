package nuc.edu.cn;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
public class MongoDBUpdateExample {
    // 连接到 MongoDB 数据库
    public static MongoDatabase connectDB() {
        // 替换为你的 MongoDB 连接字符串
        String uri = "mongodb://localhost:27017";
        MongoClient mongoClient = MongoClients.create(uri);
        return mongoClient.getDatabase("test"); // 选择数据库'test'
    }
    // 查询并输出 goodsinf 集合中的所有数据
    public static void queryAll() {
        MongoDatabase database = connectDB(); // 连接数据库
        MongoCollection<Document> collection = database.getCollection("goodsinf"); // 选择集合 'goodsinf'
        // 查询集合中的所有文档
        for (Document document : collection.find()) {
            System.out.println(document.toJson()); // 输出JSON格式的文档
        }
    }
    // 更新指定id的文档，增加出版社信息
    public static void updatePublisher(String id, String publisher) {
        MongoDatabase database = connectDB(); // 连接数据库
        MongoCollection<Document> collection = database.getCollection("goodsinf"); // 选择集合 'goodsinf'
        // 使用 Filters.eq() 查找 id 为 "001" 的文档，使用 Updates.set() 增加 publisher 字段
        UpdateResult result = collection.updateOne(Filters.eq("ljyid", id), Updates.set("publisher", publisher));
        // 输出更新结果
        if (result.getMatchedCount() > 0) {
            System.out.println("成功更新 ljyid 为 " + id + " 的文档，添加 publisher 字段。");
        } else {
            System.out.println("未找到 ljyid 为 " + id + " 的文档。");
        }
    }
    public static void querySpecificItems() {
        MongoDatabase database = connectDB(); // 连接数据库
        MongoCollection<Document> collection = database.getCollection("goodsinf"); // 获取 'goodsinf' 集合

        // 构建查询条件: price > 23 且 item 为 "小学生教材"
        FindIterable<Document> documents = collection.find(
                Filters.and(
                        Filters.gt("ljyprice", 23), // price > 23
                        Filters.eq("ljyitem", "小学生教材") // item == "小学生教材"
                )
        );

        // 输出查询结果
        System.out.println("查询结果如下：");
        for (Document doc : documents) {
            System.out.println(doc.toJson()); // 打印查询到的每个文档
        }
    }
    // 查询商品信息，并按照价格升序排序显示
    public static void queryItemsSortedByPrice() {
        MongoDatabase database = connectDB(); // 连接数据库
        MongoCollection<Document> collection = database.getCollection("goodsinf"); // 获取 'goodsinf' 集合

        // 查询并按价格升序排序
        FindIterable<Document> documents = collection.find().sort(Sorts.ascending("ljyprice"));

        // 输出查询结果
        System.out.println("按价格升序排序的商品信息如下：");
        for (Document doc : documents) {
            System.out.println(doc.toJson()); // 打印查询到的每个文档
        }
    }
    public static void deleteItemByValue(String itemValue) {
        MongoDatabase database = connectDB(); // 连接数据库
        MongoCollection<Document> collection = database.getCollection("goodsinf"); // 获取 'goodsinf' 集合

        // 删除 item 为 "初中生教材" 的文档
        DeleteResult result = collection.deleteMany(Filters.eq("ljyitem", itemValue));

        // 输出删除的文档数量
        System.out.println("删除了 " + result.getDeletedCount() + " 个文档。");
    }
    // 主函数
    public static void main(String[] args) {
        // 更新 ljyid 为 "001" 的文档，增加 publisher 字段
//        updatePublisher("001", "人民教育出版社");
        // 查询并显示所有文档
        queryAll();
//        querySpecificItems();
//        queryItemsSortedByPrice(); // 查询商品信息并按价格升序排序
        deleteItemByValue("初中生教材");
        queryAll();

    }
}