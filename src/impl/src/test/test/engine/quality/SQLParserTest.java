package engine.quality;

import engine.quality.util.SQLParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SQLParserTest {

    @Test
    public void oneParameter() {
        String test = "select count(*) from nw_orders t where {0} < t.table";
        SQLParser parser = new SQLParser(test);
        List<String> results = new ArrayList<>();
        results.add("1000");
        String parsed = parser.parametrize(results);
        assertThat(parsed).isEqualTo("select count(*) from nw_orders t where 1000 < t.table");
    }

    @Test
    public void parameterAtBeginning() {
        String test = "{0} count(*) from nw_orders t";
        SQLParser parser = new SQLParser(test);
        List<String> results = new ArrayList<>();
        results.add("1000000");
        String parsed = parser.parametrize(results);
        assertThat(parsed).isEqualTo("1000000 count(*) from nw_orders t");
    }

    @Test
    public void parameterAtEnd() {
        String test = "select count(*) from nw_orders t where t.table < {0}";
        SQLParser parser = new SQLParser(test);
        List<String> results = new ArrayList<>();
        results.add("1000000");
        String parsed = parser.parametrize(results);
        assertThat(parsed).isEqualTo("select count(*) from nw_orders t where t.table < 1000000");
    }

    @Test
    public void fourParametersWithDifferentValues() {
        String test = "{3} count {1} from {0} where {2}";
        SQLParser parser = new SQLParser(test);
        List<String> results = new ArrayList<>();
        results.add("table");
        results.add("1.234");
        results.add("-45");
        results.add("1");
        String parsed = parser.parametrize(results);
        assertThat(parsed).isEqualTo("1 count 1.234 from table where -45");
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidParameter() {
        String test = "select count(*) from nw_orders t where t.table < {4}";
        SQLParser parser = new SQLParser(test);
        List<String> results = new ArrayList<>();
        results.add("1000000");
        String parsed = parser.parametrize(results);
        assertThat(parsed).isEqualTo("select count(*) from nw_orders t where t.table < 1000000");
    }

    @Test(expected=NumberFormatException.class)
    public void invalidNumberInPlaceholder() {
        String test = "select count(*) from nw_orders t where t.table < {0aa}";
        SQLParser parser = new SQLParser(test);
        List<String> results = new ArrayList<>();
        results.add("1000000");
        String parsed = parser.parametrize(results);
        assertThat(parsed).isEqualTo("select count(*) from nw_orders t where t.table < 1000000");
    }

}
