/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 2 -*-
*   This file is part of the Java Expressions Library (JEL).
*   For more information about JEL visit : http://fti.dn.ua/JEL/
*
*   Copyright (C) 1998, 1999, 2000, 2001, 2003, 2006, 2007, 2009 Konstantin L. Metlov
*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gnu.jel;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JELTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();
	suite.addTestSuite(IntegerStackTest.class);
	suite.addTestSuite(LibraryTest.class);
	suite.addTestSuite(LibraryDotOPTest.class);
	suite.addTestSuite(ParserTest.class);
	suite.addTestSuite(ClassFileTest.class);
	suite.addTestSuite(ClassFileExprTest.class);
	suite.addTestSuite(IntegralDotOperatorTest.class);
	suite.addTestSuite(IntegralDowncastingTest.class);
	suite.addTestSuite(IntegralDynamicVariablesTest.class);
	suite.addTestSuite(IntegralErrorTest.class);
	suite.addTestSuite(IntegralExceptionsPassingTest.class);
	suite.addTestSuite(IntegralPrimitiveOPsTest.class);
	suite.addTestSuite(IntegralReferencesWideningTest.class);
	suite.addTestSuite(IntegralStaticTest.class);
	suite.addTestSuite(IntegralVarARRTest.class);
	suite.addTestSuite(IntegralVirtualTest.class);
	suite.addTestSuite(IntegralVarargsTest.class);

	/* ALERT: Don't forget to add new testsuites here before release */

        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
