package com.linzaixian.util.filter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class FilterChainTest {
	FilterChain chain=null;
	@Before
	public void setUp(){
		chain=new FilterChain();
	}
	@After
	public void TearDown(){
		
	}
	@Test
	public void testGetFilterCount() throws Exception{
		
		int size=10;
		for (int i = 0; i < size; i++) {
			chain.addFilter(new AFilter());
		}
		for (int i = 0; i < size; i++) {
			chain.addFilter(new BFilter());
		}
		try {
		    chain.doFilter(new MyParams());
		} catch (Exception e) {
		}
		Assert.assertEquals(size+1, chain.getFilterCount());
	}
	@Test
	public void testGetSuccessFilterCount() throws Exception{
		int size=10;
		for (int i = 0; i < size; i++) {
			chain.addFilter(new AFilter());
		}
		for (int i = 0; i < size; i++) {
			chain.addFilter(new BFilter());
		}
		try {
		    chain.doFilter(new MyParams());
		} catch (Exception e) {
		}
		Assert.assertEquals(size, chain.getSuccessFilterCount());
	}

	@Test
	public void testProcess() throws Exception{
		int size=10;
		for (int i = 0; i < size; i++) {
			chain.addFilter(new AFilter());
		}
		chain.doFilter(new MyParams());
		Assert.assertEquals(size, chain.getFilterCount());
	}
	@Test
	public void testProcess2() throws Exception{
		int size=10;
		for (int i = 0; i < size; i++) {
			chain.addFilter(new AFilter());
		}
		for (int i = 0; i < size; i++) {
			chain.addFilter(new BFilter());
		}
		try {
		    chain.doFilter(new MyParams());
		} catch (Exception e) {
		}
		Assert.assertEquals(size, chain.getSuccessFilterCount());
		
	}
	@Test
	public void testProcess3() throws Exception{
		int size=5;
		for (int i = 0; i < size; i++) {
			chain.addFilter(new AFilter());
		}
		
		FilterChain chain2=new FilterChain();
		for (int i = 0; i < size; i++) {
			chain2.addFilter(new AFilter());
		}
		chain.addFilter(chain2);
		for (int i = 0; i < size; i++) {
			chain.addFilter(new AFilter());
		}
		chain.doFilter(new MyParams());
		Assert.assertEquals(size*3, chain.getSuccessFilterCount());
	}
	@Test(expected=RuntimeException.class)
	public void testProcess4() throws Exception{
		FilterChain chain=new FilterChain();
		chain.addFilter(new CFilter());
		chain.doFilter(new MyParams());
	}
	@Test
	public void testProcess5() throws Exception{
		FilterChain chain=new FilterChain();
		int size=5;
		for (int i = 0; i < size; i++) {
			chain.addFilter(new DFilter());
		}
		
		FilterChain chain2=new FilterChain();
		for (int i = 0; i < size; i++) {
			chain2.addFilter(new AFilter());
		}
		chain.addFilter(chain2);
		 chain.doFilter(new MyParams());
		Assert.assertEquals(size, chain.getSuccessFilterCount());
	}
	
	
	
	public class AFilter  implements Filter{

        @Override
        public void doFilter(Params params, FilterChain filterChain) throws Exception {
            System.out.println("A");
        }
		
	}
	public class BFilter implements Filter{

        @Override
        public void doFilter(Params params, FilterChain filterChain) throws Exception {
            throw new Exception("报错了");
        }

		
	}
	public class CFilter extends CheckConfFilter{
		@Override
		public String[] getConfNames() {
			return new String[]{"不可能存在的配置项"};
		}
		@Override
		public Result validate(String confName, Object value) throws Exception {
			return Result.stop;
		}

        @Override
        public void doFilter(Params params, FilterChain filterChain) throws Exception {
            System.out.println("C");
            
        }

	}
	public class DFilter extends NotNullConfStopFilter{
		@Override
		public String[] getConfNames() {
			return new String[]{"不可能存在的配置项"};
		}

		@Override
		public Result validate(String confName, Object value) throws Exception {
			return Result.skip;
		}

        @Override
        public void doFilter(Params params, FilterChain filterChain) throws Exception {
            System.out.println("D");
            
        }
		

	}
	
	
}
