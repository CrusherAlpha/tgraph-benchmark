package common;

import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.util.List;

public class SystemInfo {
    private static void printComputerSystem(final ComputerSystem computerSystem) {
        System.out.println("System: " + computerSystem.toString());
        System.out.println(" Firmware: " + computerSystem.getFirmware().toString());
        System.out.println(" Baseboard: " + computerSystem.getBaseboard().toString());
    }

    private static void printProcessor(CentralProcessor processor) {
        System.out.println(processor.toString());
    }

    private static void printMemory(GlobalMemory memory) {
        System.out.println("Physical Memory: \n " + memory.toString());
        VirtualMemory vm = memory.getVirtualMemory();
        System.out.println("Virtual Memory: \n " + vm.toString());
        List<PhysicalMemory> pmList = memory.getPhysicalMemory();
        if (!pmList.isEmpty()) {
            System.out.println("Physical Memory: ");
            for (PhysicalMemory pm : pmList) {
                System.out.println(" " + pm.toString());
            }
        }
    }

    private static void printDisks(List<HWDiskStore> list) {
        System.out.println("Disks:");
        for (HWDiskStore disk : list) {
            System.out.println(" " + disk.toString());
        }
    }

    private static void printOperatingSystem(final OperatingSystem os) {
        System.out.println(os);
        System.out.println("Running with" + (os.isElevated() ? "" : "out") + " elevated permissions.");
    }


    private static void printJVM() {
        System.out.println(System.getProperty("java.runtime.name") + "(" + System.getProperty("java.runtime.version") + ") " +
                System.getProperty("java.vm.name") + "(" + System.getProperty("java.vm.version") + ")");
    }

    public static void systemInfo() {
        System.out.println("--------------------------------------System Info Start--------------------------------------");
        var system = new oshi.SystemInfo();
        var hardware = system.getHardware();
        System.out.println("Checking computer system...");
        printComputerSystem(hardware.getComputerSystem());
        System.out.println("Checking Processor...");
        printProcessor(hardware.getProcessor());
        System.out.println("Checking Memory...");
        printMemory(hardware.getMemory());
        System.out.println("Checking Disks...");
        printDisks(hardware.getDiskStores());
        System.out.println("Checking Operating System...");
        printOperatingSystem(system.getOperatingSystem());
        System.out.println("Checking JVM...");
        printJVM();
        System.out.println("--------------------------------------System Info End--------------------------------------");
    }

    public static void main(String[] args) {
        systemInfo();
    }
}
