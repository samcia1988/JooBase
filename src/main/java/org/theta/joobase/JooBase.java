package org.theta.joobase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.fortest.User;
import org.theta.joobase.annotations.JooCell;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JooBase {

    private static final Logger logger = LoggerFactory.getLogger(JooBase.class);

    public static synchronized void init(String pkg) throws NotFoundException {
        List<CtClass> jooClasses = scan(pkg);
        jooClasses.forEach(item -> {
            try {
                for (CtField field : item.getDeclaredFields()) {
                    String gettingName = "get" + field.getName();
                    for (CtMethod method : item.getDeclaredMethods()) {
                        if (Objects.equals(gettingName.toLowerCase(), method.getName().toLowerCase())
                                && method.getParameterTypes().length == 0) {
                            method.insertBefore("System.out.println(\"Im here\");");
                            break;
                        }
                    }
                }
                item.toClass();
            } catch (NotFoundException  | CannotCompileException  e) {
                e.printStackTrace();
            }
        });
    }

    private static List<CtClass> scan(String pkg) {
        logger.info("Start scanning JooBase classes...");
        List<CtClass> jooClasses = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();
        ScanResult scanResult = new ClassGraph().verbose().enableAllInfo().whitelistPackages(pkg).scan();
        final AtomicLong count = new AtomicLong(0);
        final AtomicLong eCount = new AtomicLong(0);
        scanResult.getClassesWithAnnotation(JooCell.class.getName()).forEach(item -> {
            logger.info("JooBase class: {}", item.getName());
            try {
                CtClass itemClass = pool.get(item.getName());
                jooClasses.add(itemClass);
                count.incrementAndGet();
            } catch (NotFoundException e) {
                logger.error("Class scanning error.", e);
                eCount.incrementAndGet();
            }
        });
        logger.info("Finish scanning JooBase classes... Succ: {},Fail: {}", count, eCount);
        return jooClasses;
    }

    public static void main(String... args) throws NotFoundException {
        init("org.theta");
        User user = new User();
        user.getName();
    }

}