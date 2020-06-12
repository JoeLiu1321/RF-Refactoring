import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;

import helper.PluginHelper;
import helper.RefactorHelper;
public class RefactorHelperTest {
	private String curDir;
	private String testDataPath;
	private RefactorHelper helper;
	private PyObject testDataRoot;
	@Before
	public void setUp() {
		initRefactorHelper();
		this.testDataPath = curDir+"/test_data/銝剜�楝敺�";
		testDataRoot = helper.buildTestData(curDir+"/test_data");
	}
	
	@After
	public void tearDown(){
		this.helper.close();
	}
	
	@Test
	public void testGetUserSelectKeyword(){
		String userTestData = testDataPath+"/robot_project/RobotTests/DCTLibrary.txt";
		String userSelectKeyword = "Login";
		PyObject keyword = helper.getKeyword(this.testDataRoot, userSelectKeyword, userTestData);
		String actualKeywordName = keyword.__getattr__("name").toString();
		assertEquals(userSelectKeyword, actualKeywordName);	
	}
	
	@Test
	public void testGetUserSelectVariable() {
		String userTestData = testDataPath+"/robot_project/RobotTests/DCTLibrary.txt";
		String userSelectVariable = "${workFlowFrame}";
		PyObject variable = helper.getVariable(testDataRoot, userSelectVariable, userTestData);
		assertNotEquals(Py.None, variable);
		String actualValue = ((PyList)variable.__getattr__("value")).get(0).toString();
		assertEquals("xpath://iframe[@id='id_wf_frame']", actualValue);
	}
	
	@Test
	public void testGetCommonKeywordReferences() {
		String userTestData = testDataPath+"/robot_project/RobotTests/DCTLibrary.txt";
		String userSelectKeyword = "Login";
		PyObject keyword = helper.getKeyword(this.testDataRoot, userSelectKeyword, userTestData);
		PyList references = helper.getKeywordReferences(this.testDataRoot, keyword);
		assertEquals(7, references.size());
	}
	
	@Test
	public void testGetSelfKeywordReferences() {
		String testData = testDataPath+"/robot_project/RobotTests/Regression Test/TMD-11538 circuit list.robot";
		String userSelectKeyword = "Circuits List::Click View Option";
		PyObject keyword = helper.getKeyword(this.testDataRoot, userSelectKeyword, testData);
		PyList references = helper.getKeywordReferences(testDataRoot, keyword);
		assertEquals(1, references.size());
		PyDictionary testDataReference = (PyDictionary)references.getArray()[0];
		PyList referencesData = (PyList)testDataReference.get(new PyString("references"));
		PyObject referencesFile = testDataReference.get(new PyString("testdata"));
		assertEquals(1, referencesData.size());
		assertEquals(Paths.get(testData).toAbsolutePath().toString(), Paths.get(referencesFile.__getattr__("source").toString()).toAbsolutePath().toString());
		PyObject firstStep = referencesData.getArray()[0];
		String referencePresent = firstStep.invoke("get_present_value").toString();
		assertEquals("Circuits List::Click View Option    ${viewName}    cus-delete-view-menu    Delete View", referencePresent);	
	}
	
	@Test
	public void testGetSelfVariableReferences() {
		String testData = testDataPath+"/robot_project/RobotTests/Regression Test/TMD-2562 Filtering list by quick cabinet name search.robot";
		String variableName = "${columnName}";
		PyObject variable = this.helper.getVariable(testDataRoot, variableName, testData);
		assertNotEquals(Py.None, variable);
		PyList references = helper.getVariableReferences(testDataRoot, variable);
		assertEquals(1, references.size());
		PyDictionary testDataReferences = (PyDictionary)references.get(0);
		PyList referencesData = (PyList)testDataReferences.get(new PyString("references"));
		assertEquals(1, referencesData.size());
		PyString firstStepPresent = (PyString)referencesData.getArray()[0].invoke("get_present_value");
		assertEquals("Items List::Filter Items By Cabinet    ${columnName}", firstStepPresent.toString());
	}
	
	@Test
	public void testGetCommonVariableReferences() {
		String testData = testDataPath+"/robot_project/RobotTests/DCTLibrary.txt";
		String variableName = "${modelsLibraryFrame}";
		PyObject variable = this.helper.getVariable(testDataRoot, variableName, testData);
		assertNotEquals(Py.None, variable);
		PyList references = helper.getVariableReferences(testDataRoot, variable);
		assertEquals(26, references.size());
	}
	
	@Test
	public void testWithUnicodeProject(){
		String userTestData = testDataPath+"/robot_project/RobotTests/DCTLibrary.txt";
		String userSelectKeyword = "Login";
		PyObject keyword = helper.getKeyword(this.testDataRoot, userSelectKeyword, userTestData);
		String actualKeywordName = keyword.__getattr__("name").toString();
		assertEquals(userSelectKeyword, actualKeywordName);	
	}
	
	public void initRefactorHelper() {
		String pythonSite = "C:/Users/lab1321/AppData/Local/Programs/Python/Python37/Lib/site-packages";
		String jythonPath = pythonSite+"/rfrefactoring/jython-standalone-2.7.2b3.jar";
		PluginHelper.initPython(jythonPath, pythonSite);
		this.curDir = System.getProperty("user.dir");
		this.helper = new RefactorHelper(new String[] {});
	}
	
}
