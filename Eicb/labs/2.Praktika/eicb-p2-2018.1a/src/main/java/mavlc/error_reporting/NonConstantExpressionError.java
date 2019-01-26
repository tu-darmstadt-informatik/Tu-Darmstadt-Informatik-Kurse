/*******************************************************************************
 * Copyright (C) 2016-2018 Embedded Systems and Applications Group
 * Department of Computer Science, Technische Universitaet Darmstadt,
 * Hochschulstr. 10, 64289 Darmstadt, Germany.
 * 
 * All rights reserved.
 * 
 * This software is provided free for educational use only.
 * It may not be used for commercial purposes without the
 * prior written permission of the authors.
 ******************************************************************************/
package mavlc.error_reporting;

import mavlc.ast.nodes.expression.Expression;

/**
 * Error class to signal that an expression that must be constant wasn't actually a constant expression.
 */
public class NonConstantExpressionError extends CompilationError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6099076852045473097L;

	private final Expression expr;
	
	/**
	 * Constructor.
	 * @param expr The non-constant expr expression.
	 */
	public NonConstantExpressionError(Expression expr){
		this.expr = expr;
		
		StringBuilder sb = new StringBuilder();
		sb.append("Error @ \"").append(this.expr.dump()).append("\" in line ").append(this.expr.getSrcLine());
		sb.append(", column ").append(this.expr.getSrcColumn()).append(": \n");
		sb.append("Not a constant expression: ").append(this.expr.dump());
		this.message = sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expr == null) ? 0 : expr.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NonConstantExpressionError other = (NonConstantExpressionError) obj;
		if (expr == null) {
			if (other.expr != null)
				return false;
		} else if (!expr.equals(other.expr))
			return false;
		return true;
	}
}
