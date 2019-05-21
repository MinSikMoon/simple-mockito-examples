# simple-mockito-examples
- very simple mockito examples
- 쉽고 간단한 mockito 예제
- library
![image](https://user-images.githubusercontent.com/21155325/58033908-571b6200-7b60-11e9-95a9-c7ef88a6d59e.png)

````java
package test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.lessThan;
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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import main.DaoA;
import main.DaoB;
import main.ServiceA;
@RunWith(MockitoJUnitRunner.class)
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
	@Test 
	public void tc10_hamcrest_matchers() {
		List<Integer> numbers = Arrays.asList(10, 11, 20, 30);
		assertThat(numbers, hasSize(4));
		assertThat(numbers, hasItems(10,30));
		assertThat(numbers, everyItem(greaterThan(9)));
		assertThat(numbers, everyItem(lessThan(31)));
	}
	@Test 
	public void tc11_hamcrest_matchers2() {
		assertThat("", isEmptyString());
		assertThat(null, isEmptyOrNullString());
		Integer[] numbs = {1,2,3};
		assertThat(numbs, arrayContaining(1,2,3)); //순서와 인자값 다 같아야 통과
		assertThat(numbs, arrayContainingInAnyOrder(3,1,2)); //순서와 인자값 다 같아야 통과
	}
	
	
	@Mock
	DaoA da;
	@Mock
	DaoB db;
	@InjectMocks
	ServiceA sa;
	/**
	ServiceA sa = new ServiceA();
	sa.setDaoA(mock(DaoA.class));
	sa.setDaoB(mock(DaoB.class));
	 * 
	 */
	@Test
	public void tc12_annotation() {
		/*ServiceA sa = mock(ServiceA.class); //serviceA의 mock을 만든다. */
		when(sa.getSum()).thenReturn(3);    //mock이기에 getSum()하면 0 나온다. 그래서 getSum()하면 3나오게 시킨다.
		assertEquals(3, sa.getSum());
	}
	@Captor
	ArgumentCaptor<String> sac;
	@Test 
	public void tc13_captor() {
		//given
		given(da.getBoolean()).willReturn(true); //false로 하면 da가 불려지지 않아서 fail이다.
		//when
		sa.getInnerBoolean(da);
		verify(da).getStr(sac.capture());
		//then
		assertThat(sac.getValue(), is("yes"));
	}
}
````
