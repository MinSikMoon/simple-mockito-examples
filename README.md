# simple-mockito-examples
very simple mockito examples


````java
package test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import main.DaoA;
import main.ServiceA;

public class MockTest1 {
	@Test
	public void tc1() {
		ServiceA sa = mock(ServiceA.class); //serviceA의 mock을 만든다.
		when(sa.getSum()).thenReturn(3);    //mock이기에 getSum()하면 0 나온다. 그래서 getSum()하면 3나오게 시킨다.
		assertEquals(3, sa.getSum());
	}
	@Test
	public void tc2_thenReturn() {
		List tlist = mock(List.class); 
		when(tlist.get(0)).thenReturn("hihi");
		when(tlist.get(1)).thenReturn(2);
		assertEquals("hihi", tlist.get(0));
		assertEquals(2, tlist.get(1));
		assertEquals(null, tlist.get(2)); //특정 리턴을 지정하지 않으면 default값을 준다. 
	}
	@Test
	public void tc3_anyInt() {
		List tlist = mock(List.class); 
		when(tlist.get(anyInt())).thenReturn("hihi");
		assertEquals("hihi", tlist.get(0));
		assertEquals("hihi", tlist.get(1));
		assertEquals("hihi", tlist.get(2)); //특정 리턴을 지정하지 않으면 default값을 준다. 
	}
	@Test(expected=RuntimeException.class)
	public void tc4_Exception() {
		List tlist = mock(List.class); 
		when(tlist.get(anyInt())).thenThrow(new RuntimeException());
		tlist.get(777);
	}
	@Test(expected=RuntimeException.class)
	public void tc5_GivenWhenThen() {
		//given = setup
		List tlist = mock(List.class); 
		//when - thenReturn // given - willReturn
		given(tlist.get(anyInt())).willThrow(new RuntimeException());
		//then
		tlist.get(777);
	}
	@Test 
	public void tc6_assertThatIs() {
		List<String> tlist = mock(List.class); 
		when(tlist.get(anyInt())).thenReturn("hihi");
		assertEquals("hihi", tlist.get(0));
		assertThat(tlist.get(777), is("hihi")); //is는 hamcrest matcher 인데 junit 라이브러리보다 먼저 읽혀야 한다.
	}
	@Test 
	public void tc7_verifyMock_called() {
		ServiceA sa = new ServiceA();
		DaoA da = mock(DaoA.class);
		given(da.getBoolean()).willReturn(true); //false로 하면 da가 불려지지 않아서 fail이다.
		sa.getInnerBoolean(da);
		verify(da).getNumber();
		verify(da,times(1)).getNumber(); //2번불려지지 않으니까 2번하면 실패
		verify(da,atLeast(1)).getNumber();
	}
	@Test 
	public void tc8_verifyMock_never_called() {
		ServiceA sa = new ServiceA();
		DaoA da = mock(DaoA.class); 
		given(da.getBoolean()).willReturn(false); //false로 하면 da가 불려지지 않아서 fail이다.
		sa.getInnerBoolean(da);
		verify(da,never()).getNumber();
	}
	@Test 
	public void tc9_argument_capture() {
		//declare argument captor
		//define argument captor on specific method call
		//capture the argument

		//given
		ArgumentCaptor<String> sac = ArgumentCaptor.forClass(String.class);
		ServiceA sa = new ServiceA();
		DaoA da = mock(DaoA.class); 
		given(da.getBoolean()).willReturn(true); //false로 하면 da가 불려지지 않아서 fail이다.
		//when
		sa.getInnerBoolean(da);
		verify(da).getStr(sac.capture());
		
		//then
		assertThat(sac.getValue(), is("yes"));
	}

}


````