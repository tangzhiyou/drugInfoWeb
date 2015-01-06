package com.skysoft.JavaANDPython;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * User: pinaster
 * Date: 13-12-1
 * Time: 下午1:15
 */
public class PythonExtractHtml {
    public static void main(String[] args)
    {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("python/extractHtml.py");
        PyFunction func = (PyFunction)interpreter.get("adder",PyFunction.class);

        int a = 2010, b = 2 ;
        PyObject pyobj = func.__call__(new PyInteger(a), new PyInteger(b));
        System.out.println("anwser = " + pyobj.toString());
    }
}
