
package com.funnel.svc.comon;

import org.apache.log4j.Logger;

import com.funnel.svc.Constant;
import com.funnel.svc.SvcContext;
import com.funnel.svc.SvcException;

/**
 * 异步服务执行线程
 * 
 * @author wanghua4
 */
public class AsyncSvcExecuteThread implements Runnable, AsyncSvcExecutor
{
    protected final Logger logger = Logger.getLogger(this.getClass());

    // 当前要执行的请求上下文
    private SvcContext context;

    private AsyncService svc;

    public AsyncSvcExecuteThread(AsyncService svc, SvcContext context)
    {
        this.svc = svc;
        this.context = context;
    }

    @Override
    public void run()
    {
        long begin = System.currentTimeMillis();
        try
        {
            // logger.debug("开始执行序列号:"+context.getSeqno());
            svc.enter();
            svc.doBefore(context);
            // 执行业务处理
            svc.process(context);

            svc.doAfter(context);
            long endtm = System.currentTimeMillis();
            setExcuteTime(endtm - begin);
            svc.exit();
            if ((endtm - begin) > 30000)
            {
                logger.info("服务svcCode:" + context.getSvcCode() + " seq:" + context.getSeqno() + ",执行业务方法时间："
                    + (endtm - begin) + "ms");
            }
            // logger.debug("执行序列号:"+context.getSeqno()+"完成");
        }
        catch (Exception e)
        {
            handleException(e);
            svc.onFail(context);
            svc.doError(context);
            svc.term();
            long endtm = System.currentTimeMillis();
            setExcuteTime(endtm - begin);
            logger.error("服务 svcCode:" + context.getSvcCode() + " seq:" + context.getSeqno() + ",执行失败处理方法时间："
                + (endtm - begin) + "ms");
            // logger.debug("执行序列号:" + context.getSeqno() + "异常", e);
        }
    }

    private void setExcuteTime(long time)
    {
        svc.addExcuteTime(time);
        svc.setMaxExcuteTime(time);
    }

    /**
     * 处理失败
     * 
     * @param e
     *            异常对象
     */
    private void handleException(Exception e)
    {
        logger.error("执行服务失败, svcCode:" + context.getSvcCode() + " seq:" + context.getSeqno(), e);
        if (e instanceof SvcException)
        {
            context.setFailException((SvcException)e);
        }
        else
        {
            context.setFailException(new SvcException(Constant.ERROR_CODE_SYS_ERROR, e));
        }
    }

    public SvcContext getContext()
    {
        return context;
    }

    @Override
    public void afterThreadPoolRefuse()
    {
        logger.debug("服务被拒绝, svcCode:" + context.getSvcCode() + " seq:" + context.getSeqno());
    }

}
