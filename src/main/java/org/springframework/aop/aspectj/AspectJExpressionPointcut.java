package org.springframework.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {
    //支持的切点原语集合
    private static final Set<PointcutPrimitive> SUPPPORTED_PRIMITIVES = new HashSet<>();

    static {
        //添加支持的切点原语，在此示例中，仅支持EXECUTION原语，可以根据需要添加更多
        SUPPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }
    //切点表达式对象，用于解析和匹配切点
    private final PointcutExpression pointcutExpression;

    /**
     * 构造函数，用给定的表达式创建AspectJ表达式切点
     * @param expression
     */
    public AspectJExpressionPointcut(String expression){
        //创建一个PointcutParser对象，用于解析表达式
        PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                SUPPPORTED_PRIMITIVES, this.getClass().getClassLoader());
        //解析给定的切点表达式，并将其分配给成员变量pointcutExpression
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * 检查给定的类是否符合切点表达式的条件
     * @param clazz
     * @return
     */
    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    /**
     * 检查给定的方法是否符合切点表达式的条件
     * @param method
     * @param targetClass
     * @return
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        //使用切点表达式检查方法执行是否匹配
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    /**
     * 获取用于类筛选的ClassFilter实例
     * @return
     */
    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    /**
     * 获取用于方法匹配的MethodMatcher实例
     * @return
     */
    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
