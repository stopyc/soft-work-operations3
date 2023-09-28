import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void test(){
        /*List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.remove(0);
        System.out.println(list.get(0));*/
        Arrays.stream("4 × 3/6".split("\\s+")).forEach(System.out::println);
    }
}