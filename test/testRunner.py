import unittest
from os import path
import sys
p = path.normpath(path.dirname(path.abspath(__file__))+"/../..")
sys.path.append(p)
print(p)
from usageFinderTest import KeywordUsageFinderTest, VariableUsageFinderTest
from testDataDependencyBuilderTest import TestDataDependencyBuilderTest
from referencesMethodsTest import ReferencesMethodsTest
from refactoringFacadeTest import RefactoringFacadeTest
if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(KeywordUsageFinderTest)
    suite.addTest(unittest.TestLoader().loadTestsFromTestCase(VariableUsageFinderTest))
    suite.addTest(unittest.TestLoader().loadTestsFromTestCase(TestDataDependencyBuilderTest))
    suite.addTest(unittest.TestLoader().loadTestsFromTestCase(ReferencesMethodsTest))
    suite.addTest(unittest.TestLoader().loadTestsFromTestCase(RefactoringFacadeTest))
    unittest.TextTestRunner(verbosity=2).run(suite)