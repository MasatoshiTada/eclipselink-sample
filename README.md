# 実行環境
- macOS Sierra 10.12.4
- Oracle JDK 8u51
- MySQL 5.6.34 Homebrew
- EclipseLink 2.6.4
- mysql-connector-java 6.0.6

# SQL

Mainクラスを実行すると、下記のSQLが自動的に実行されます。

```sql
CREATE TABLE sample ( id INTEGER PRIMARY KEY AUTO_INCREMENT, text VARCHAR(32));

CREATE TABLE sequence( seq_name VARCHAR(64) PRIMARY KEY, seq_count INTEGER);

INSERT INTO sequence(seq_name, seq_count) VALUES('SEQ_GEN', 0);
```

# エンティティ

```java
@Entity
public class Sample implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String text;

    // setter/getter/コンストラクタ等省略
```

# Mainクラス

```java
public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("samplePU");
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Sample sample = new Sample();
            sample.setId(999); // TODO IDに値をセットするとダメ？
            sample.setText("aaa");

            em.persist(sample);
            em.getTransaction().commit();

            System.out.println(sample + "が正常にINSERTされました。");
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                em.getTransaction().rollback();
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            e.printStackTrace();
        }
    }
}
```

# 実行結果（IDに値をセットしない場合）

- Mainクラス

```java
Sample sample = new Sample();
//  sample.setId(999); // TODO IDに値をセットするとダメ？
sample.setText("aaa");

```

- 実行結果

```
[EL Fine]: server: 2017-04-14 16:12:24.998--Thread(Thread[main,5,main])--Configured server platform: org.eclipse.persistence.platform.server.NoServerPlatform
[EL Config]: metadata: 2017-04-14 16:12:25.085--ServerSession(1547425104)--Thread(Thread[main,5,main])--The access type for the persistent class [class com.example.entity.Sample] is set to [FIELD].
[EL Config]: metadata: 2017-04-14 16:12:25.098--ServerSession(1547425104)--Thread(Thread[main,5,main])--The alias name for the entity class [class com.example.entity.Sample] is being defaulted to: Sample.
[EL Config]: metadata: 2017-04-14 16:12:25.1--ServerSession(1547425104)--Thread(Thread[main,5,main])--The table name for entity [class com.example.entity.Sample] is being defaulted to: SAMPLE.
[EL Config]: metadata: 2017-04-14 16:12:25.109--ServerSession(1547425104)--Thread(Thread[main,5,main])--The column name for element [id] is being defaulted to: ID.
[EL Config]: metadata: 2017-04-14 16:12:25.11--ServerSession(1547425104)--Thread(Thread[main,5,main])--The column name for element [text] is being defaulted to: TEXT.
[EL Info]: 2017-04-14 16:12:25.502--ServerSession(1547425104)--Thread(Thread[main,5,main])--EclipseLink, version: Eclipse Persistence Services - 2.6.4.v20160829-44060b6
[EL Fine]: connection: 2017-04-14 16:12:25.65--Thread(Thread[main,5,main])--Detected database platform: org.eclipse.persistence.platform.database.MySQLPlatform
[EL Config]: connection: 2017-04-14 16:12:25.656--ServerSession(1547425104)--Connection(1908505175)--Thread(Thread[main,5,main])--connecting(DatabaseLogin(
	platform=>MySQLPlatform
	user name=> "root"
	datasource URL=> "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8"
))
[EL Config]: connection: 2017-04-14 16:12:25.659--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--Connected: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8
	User: root@localhost
	Database: MySQL  Version: 5.6.34
	Driver: MySQL Connector Java  Version: mysql-connector-java-6.0.6 ( Revision: 3dab84f4d9bede3cdd14d57b99e9e98a02a5b97d )
[EL Info]: connection: 2017-04-14 16:12:25.689--ServerSession(1547425104)--Thread(Thread[main,5,main])--/file:/Users/tadamasatoshi/IdeaProjects/eclipselink-sample/target/classes/_samplePU login successful
[EL Fine]: sql: 2017-04-14 16:12:25.691--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--DROP TABLE IF EXISTS sequence;
[EL Fine]: sql: 2017-04-14 16:12:25.698--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--DROP TABLE IF EXISTS sample;
[EL Fine]: sql: 2017-04-14 16:12:25.7--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--CREATE TABLE sample ( id INTEGER PRIMARY KEY AUTO_INCREMENT, text VARCHAR(32));
[EL Fine]: sql: 2017-04-14 16:12:25.72--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--CREATE TABLE sequence( seq_name VARCHAR(64) PRIMARY KEY, seq_count INTEGER);
[EL Fine]: sql: 2017-04-14 16:12:25.735--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--INSERT INTO sequence(seq_name, seq_count) VALUES('SEQ_GEN', 0);
[EL Fine]: sql: 2017-04-14 16:12:25.752--ClientSession(38625764)--Connection(1047000562)--Thread(Thread[main,5,main])--UPDATE SEQUENCE SET SEQ_COUNT = SEQ_COUNT + ? WHERE SEQ_NAME = ?
	bind => [50, SEQ_GEN]
[EL Fine]: sql: 2017-04-14 16:12:25.753--ClientSession(38625764)--Connection(1047000562)--Thread(Thread[main,5,main])--SELECT SEQ_COUNT FROM SEQUENCE WHERE SEQ_NAME = ?
	bind => [SEQ_GEN]
[EL Fine]: sql: 2017-04-14 16:12:25.759--ClientSession(38625764)--Connection(1047000562)--Thread(Thread[main,5,main])--INSERT INTO SAMPLE (ID, TEXT) VALUES (?, ?)
	bind => [1, aaa]
Sample{id=1, text='aaa'}が正常にINSERTされました。
```


# 実行結果（IDに値をセットした場合）

- Mainクラス

```java
Sample sample = new Sample();
sample.setId(999); // TODO IDに値をセットするとダメ？
sample.setText("aaa");

```

- 実行結果

```
[EL Fine]: server: 2017-04-14 16:13:23.621--Thread(Thread[main,5,main])--Configured server platform: org.eclipse.persistence.platform.server.NoServerPlatform
[EL Config]: metadata: 2017-04-14 16:13:23.71--ServerSession(1547425104)--Thread(Thread[main,5,main])--The access type for the persistent class [class com.example.entity.Sample] is set to [FIELD].
[EL Config]: metadata: 2017-04-14 16:13:23.724--ServerSession(1547425104)--Thread(Thread[main,5,main])--The alias name for the entity class [class com.example.entity.Sample] is being defaulted to: Sample.
[EL Config]: metadata: 2017-04-14 16:13:23.725--ServerSession(1547425104)--Thread(Thread[main,5,main])--The table name for entity [class com.example.entity.Sample] is being defaulted to: SAMPLE.
[EL Config]: metadata: 2017-04-14 16:13:23.736--ServerSession(1547425104)--Thread(Thread[main,5,main])--The column name for element [id] is being defaulted to: ID.
[EL Config]: metadata: 2017-04-14 16:13:23.737--ServerSession(1547425104)--Thread(Thread[main,5,main])--The column name for element [text] is being defaulted to: TEXT.
[EL Info]: 2017-04-14 16:13:24.115--ServerSession(1547425104)--Thread(Thread[main,5,main])--EclipseLink, version: Eclipse Persistence Services - 2.6.4.v20160829-44060b6
[EL Fine]: connection: 2017-04-14 16:13:24.277--Thread(Thread[main,5,main])--Detected database platform: org.eclipse.persistence.platform.database.MySQLPlatform
[EL Config]: connection: 2017-04-14 16:13:24.283--ServerSession(1547425104)--Connection(1908505175)--Thread(Thread[main,5,main])--connecting(DatabaseLogin(
	platform=>MySQLPlatform
	user name=> "root"
	datasource URL=> "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8"
))
[EL Config]: connection: 2017-04-14 16:13:24.286--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--Connected: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8
	User: root@localhost
	Database: MySQL  Version: 5.6.34
	Driver: MySQL Connector Java  Version: mysql-connector-java-6.0.6 ( Revision: 3dab84f4d9bede3cdd14d57b99e9e98a02a5b97d )
[EL Info]: connection: 2017-04-14 16:13:24.315--ServerSession(1547425104)--Thread(Thread[main,5,main])--/file:/Users/tadamasatoshi/IdeaProjects/eclipselink-sample/target/classes/_samplePU login successful
[EL Fine]: sql: 2017-04-14 16:13:24.317--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--DROP TABLE IF EXISTS sequence;
[EL Fine]: sql: 2017-04-14 16:13:24.326--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--DROP TABLE IF EXISTS sample;
[EL Fine]: sql: 2017-04-14 16:13:24.327--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--CREATE TABLE sample ( id INTEGER PRIMARY KEY AUTO_INCREMENT, text VARCHAR(32));
[EL Fine]: sql: 2017-04-14 16:13:24.345--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--CREATE TABLE sequence( seq_name VARCHAR(64) PRIMARY KEY, seq_count INTEGER);
[EL Fine]: sql: 2017-04-14 16:13:24.358--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--INSERT INTO sequence(seq_name, seq_count) VALUES('SEQ_GEN', 0);
[EL Fine]: sql: 2017-04-14 16:13:24.377--ServerSession(1547425104)--Connection(1047000562)--Thread(Thread[main,5,main])--SELECT ID FROM SAMPLE WHERE (ID = ?)
	bind => [999]
[EL Fine]: sql: 2017-04-14 16:13:24.384--ClientSession(1134599394)--Connection(1047000562)--Thread(Thread[main,5,main])--INSERT INTO SAMPLE (ID, TEXT) VALUES (?, ?)
	bind => [999, aaa]
Sample{id=999, text='aaa'}が正常にINSERTされました。
```