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

/* TODO: Please fill this out!
 *
 * EiCB group number:
 * Names and student ID numbers of group members:
 */

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

    // TODO: implement (exercise 2.6)
}
