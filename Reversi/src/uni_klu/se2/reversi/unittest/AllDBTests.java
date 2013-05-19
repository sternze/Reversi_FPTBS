package uni_klu.se2.reversi.unittest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ H2DBGameDAOTest.class, H2DBGameUserDAOTest.class,
		H2DBUserDAOTest.class })
public class AllDBTests {
}
