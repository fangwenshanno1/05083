package com.atguigu.jedis.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

public class JedisTest {

	
	// redis-cli -h xx -p xxx
	// 命令
	
	// 超时的原因：  ①检查服务端bind的地址是否匹配  ②检查保护模式是否开启  ③服务器防火墙是否开启
	 
	@Test
	public void test() {
		
		// 创建客户端对象
		
		Jedis jedis = new Jedis("192.168.6.5",6379,20000);
		
		// 使用客户端对象，调用方法
		String result = jedis.ping();
		
		System.out.println(result);
		
		System.out.println(result);
		
		// Jedis使用完毕及时关闭
		
		jedis.close();
		
	}
	
	// 使用连接池获取Jedis连接
	@Test
	public void testPool(){
		//默认的连接池配置
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		
		System.out.println(poolConfig);
		// 创建一个JedisPool
		JedisPool jedisPool=new JedisPool(poolConfig, "192.168.6.5", 6379,60000);
		
		Jedis jedis = jedisPool.getResource();
		
		String ping = jedis.ping();
		
		System.out.println(ping);
		//如果是从连接池中获取的，那么执行close方法只是将连接放回到池中
		jedis.close();
		
		jedisPool.close();}
	
	// redis使用主从配置后，只需要连接哨兵的服务
	@Test
	public void testSentinel() throws Exception {
		
		Set<String> set = new HashSet<>();
		// set中放的是哨兵的Ip和端口
		set.add("192.168.6.5:26379");
		
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		
		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster", set, poolConfig, 60000);
		
		Jedis jedis = jedisSentinelPool.getResource();
		
		String value = jedis.get("k3");
		
		jedis.set("Jedis1", "Jedis");
		
		System.out.println(value);
	}
	
	
	@Test
	public void testCluster(){
		
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		//Jedis Cluster will attempt to discover cluster nodes automatically
		
		jedisClusterNodes.add(new HostAndPort("192.168.6.5", 6379));
		jedisClusterNodes.add(new HostAndPort("192.168.6.5", 6380));
		
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		
		jc.set("foo", "bar");
		
		String value = jc.get("foo");
		
		System.out.println(value);

		System.out.println("hello");
		System.out.println(value);
	}




}
