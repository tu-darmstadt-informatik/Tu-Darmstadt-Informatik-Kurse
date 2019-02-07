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
package mavlc.context_analysis;

import mavlc.ast.nodes.ASTNode;
import mavlc.ast.nodes.expression.*;
import mavlc.ast.visitor.ASTNodeBaseVisitor;
import mavlc.error_reporting.NonConstantExpressionError;

public class ConstantExpressionEvaluator extends ASTNodeBaseVisitor<Integer, Void> {
    @Override
    protected Integer defaultOperation(ASTNode node, Void obj) {
        if (node instanceof Expression) {
            throw new NonConstantExpressionError((Expression) node);
        } else {
            throw new RuntimeException("Internal compiler error: should not try to constant-evaluate non-expressions");
        }
    }

    @Override
    public Integer visitIntValue(IntValue intValue, Void __) {
        return intValue.getValue();
    }

    @Override
    public Integer visitUnaryMinus(UnaryMinus unaryMinus, Void __) {
        return -unaryMinus.getOperand().accept(this, __);
    }

    @Override
    public Integer visitAddition(Addition addition, Void __) {
        return addition.getLeftOp().accept(this, __) + addition.getRightOp().accept(this, __);
    }

    @Override
    public Integer visitSubtraction(Subtraction subtraction, Void __) {
        return subtraction.getLeftOp().accept(this, __) - subtraction.getRightOp().accept(this, __);
    }

    @Override
    public Integer visitMultiplication(Multiplication multiplication, Void __) {
        return multiplication.getLeftOp().accept(this, __) * multiplication.getRightOp().accept(this, __);
    }

    @Override
    public Integer visitDivision(Division division, Void __) {
        return division.getLeftOp().accept(this, __) / division.getRightOp().accept(this, __);
    }

    @Override
    public Integer visitExponentiation(Exponentiation exponentiation, Void __) {
        int base = exponentiation.getLeftOp().accept(this, __);
        int exponent = exponentiation.getRightOp().accept(this, __);
        int pow = 1;
        for (int i = 0; i < exponent; ++i) {
            pow *= base;
        }
        return pow;
    }
}
