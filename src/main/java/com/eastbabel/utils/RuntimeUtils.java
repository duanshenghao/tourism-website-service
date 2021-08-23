package com.eastbabel.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统环境工具类
 * Ref. https://docs.oracle.com/javase/10/docs/api/com/sun/management/OperatingSystemMXBean.html#method.summary
 */
public class RuntimeUtils {

    private static final OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final Runtime runtime = Runtime.getRuntime();

    /**
     * 获取物理内存总大小
     */
    public static long getTotalPhysicalMemorySize() {
        return systemMXBean.getTotalPhysicalMemorySize();
    }

    /**
     * 获取物理内存剩余大小
     */
    public static long getFreePhysicalMemorySize() {
        return systemMXBean.getFreePhysicalMemorySize();
    }

    /**
     * 获取物理内存已使用大小
     */
    public static long getUsedPhysicalMemorySize() {
        return systemMXBean.getTotalPhysicalMemorySize() - systemMXBean.getFreePhysicalMemorySize();
    }

    /**
     * 获取 Swap 总大小
     */
    public static long getTotalSwapSpaceSize() {
        return systemMXBean.getTotalSwapSpaceSize();
    }

    /**
     * 获取 Swap 剩余大小
     */
    public static long getFreeSwapSpaceSize() {
        return systemMXBean.getFreeSwapSpaceSize();
    }

    /**
     * 获取 Swap 已使用大小
     */
    public static long getUsedSwapSpaceSize() {
        return systemMXBean.getTotalSwapSpaceSize() - systemMXBean.getFreeSwapSpaceSize();
    }

    /**
     * 获取 JVM 最大内存
     */
    public static long getJvmMaxMemory() {
        return runtime.maxMemory();
    }

    /**
     * 获取 JVM 内存总大小
     */
    public static long getJvmTotalMemory() {
        return runtime.totalMemory();
    }

    /**
     * 获取 JVM 内存剩余大小
     */
    public static long getJvmFreeMemory() {
        return runtime.freeMemory();
    }

    /**
     * 获取 JVM 内存已使用大小
     */
    public static long getJvmUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * 获取系统 CPU 使用率
     */
    public static double getSystemCpuLoad() {
        return systemMXBean.getSystemCpuLoad();
    }

    /**
     * 获取 JVM 进程 CPU 使用率
     */
    public static double getProcessCpuLoad() {
        return systemMXBean.getProcessCpuLoad();
    }

    public static String getHostName() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "default-server";
        }
    }

}