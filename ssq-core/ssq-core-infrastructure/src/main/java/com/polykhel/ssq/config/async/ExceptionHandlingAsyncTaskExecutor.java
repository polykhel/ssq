package com.polykhel.ssq.config.async;

import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ExceptionHandlingAsyncTaskExecutor class.
 */
public class ExceptionHandlingAsyncTaskExecutor
    implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    private static final Logger log = getLogger(ExceptionHandlingAsyncTaskExecutor.class);
    static final String EXCEPTION_MESSAGE = "Caught async exception";

    private final AsyncTaskExecutor executor;

    /**
     * Constructor for ExceptionHandlingAsyncTaskExecutor.
     *
     * @param executor an AsyncTaskExecutor object.
     */
    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean) {
            DisposableBean bean = (DisposableBean) executor;
            bean.destroy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean) {
            InitializingBean bean = (InitializingBean) executor;
            bean.afterPropertiesSet();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable task) {
        executor.execute(handleRunnable(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(handleRunnable(task), startTimeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(handleRunnable(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(handleCallable(task));
    }

    private Runnable handleRunnable(final Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handle(e);
            }
        };
    }

    private <T> Callable<T> handleCallable(final Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                handle(e);
                throw e;
            }
        };
    }

    /**
     * Handle exception.
     *
     * @param e an Exception object.
     */
    protected void handle(Exception e) {
        log.error(EXCEPTION_MESSAGE, e);
    }
}
