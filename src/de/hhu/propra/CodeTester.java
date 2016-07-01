package de.hhu.propra;

import vk.core.api.CompilationUnit;
import vk.core.api.CompileError;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

public class CodeTester {
	private static JavaStringCompiler compiler;
	
	
	public static void testCode(String code){
		CompilationUnit unit1 = new CompilationUnit("Test", code, false);
		compiler = CompilerFactory.getCompiler(unit1);
		
		compiler.compileAndRunTests();
		
		if (compiler.getCompilerResult().hasCompileErrors()){
			for (CompileError test : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit1)){
				System.out.println(test);
			}
		}
	}
}
