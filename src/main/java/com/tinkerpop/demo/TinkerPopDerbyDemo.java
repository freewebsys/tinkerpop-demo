package com.tinkerpop.demo;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.wingnest.blueprints.impls.jpa.JpaGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class TinkerPopDerbyDemo {

    private JpaGraph jpaGraph = null;

    @Before
    public void setUp() {
        //初始化jpa。保存到数据库中。使用hibernate 自动创建表结构。
        //如果要使用mysql，这里修改属性。
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.persistence.jdbc.url", String.format("jdbc:derby:db/HibernateUnit_test_perf;create=true"));
        jpaGraph = new JpaGraph("HibernateUnit", props);
    }

    @After
    public void tearDown() throws Exception {
        //关闭
        jpaGraph.shutdown();
    }

    @Test
    public void testCreate() {

        //创建张三数据
        Vertex zhangsan = jpaGraph.addVertex(null);
        zhangsan.setProperty("name", "zhangsan");
        System.out.println("zhangsan:" + zhangsan.getId());

        //创建李四数据
        Vertex lisi = jpaGraph.addVertex(null);
        lisi.setProperty("name", "lisi");
        System.out.println("lisi:" + lisi.getId());

        //创建王五数据
        Vertex wangwu = jpaGraph.addVertex(null);
        wangwu.setProperty("name", "wangwu");
        System.out.println("wangwu:" + wangwu.getId());

        //设置李四和王五朋友关系，friend是连接的名字，可以随意取。
        Edge friend1 = jpaGraph.addEdge(null, zhangsan, lisi, "friend");

        //设置王五和李四朋友关系
        Edge friend2 = jpaGraph.addEdge(null, wangwu, lisi, "friend");

        System.out.println("create finish");
    }

    @Test
    public void testQuery() {
        //查询全部数据。
        queryAll();
        queryZhansanFriends();
        System.out.println("query finish");
    }

    private void queryAll() {
        Iterable<Vertex> allVertex = jpaGraph.getVertices();
        System.out.println("######################query all######################");
        for (Vertex vertex : allVertex) {
            System.out.print("name:" + vertex.getProperty("name"));
            System.out.println(",id:" + vertex.getId());
        }
    }

    private void queryZhansanFriends() {
        Vertex zhangsan = jpaGraph.getVertex(1);
        System.out.println("######################query zhangsan friends######################");
        Iterable<Vertex> zhansanFriends = zhangsan.getVertices(Direction.OUT, "friend");
        for (Vertex vertex : zhansanFriends) {
            System.out.print("name:" + vertex.getProperty("name"));
            System.out.println(",id:" + vertex.getId());
        }
    }

    @Test
    public void testDelete() {
        Vertex lisi = jpaGraph.getVertex(2);
        jpaGraph.removeVertex(lisi);
        //删除之后，查询全部。
        queryAll();
        queryZhansanFriends();
    }

}