// BuddySystem.java

import java.util.Scanner;

// BuddySystem class simulates the buddy system memory allocation
public class BuddySystem {
    private static int[] freeBlocks; // Array storing the count of free blocks of each size
    private static final int MAX_MEMORY = 1024; // Total memory available in KB
    private static final int MAX_BLOCK_SIZE = 1024; // Maximum block size (1024KB)
    
    // Initialize Buddy System memory pool
    public static void initializeBuddySystem(int totalMemory) {
        int maxSize = nextPowerOf2(totalMemory);  // Find largest power of 2 less than total memory
        int maxBlocks = log2(maxSize) + 1; // Number of different block sizes (max size + 1)
        
        freeBlocks = new int[maxBlocks]; // Allocate space for block sizes
        
        // Initially, there is one free block of the largest size
        freeBlocks[maxBlocks - 1] = 1;
    }
    
    // Function to calculate the next power of 2 greater than or equal to the given size
    public static int nextPowerOf2(int size) {
        return (int) Math.pow(2, Math.ceil(Math.log(size) / Math.log(2)));
    }
    
    // Function to calculate log base 2
    public static int log2(int num) {
        return (int)(Math.log(num) / Math.log(2));
    }

    // Function to allocate memory of a specific job size
    public static void allocateJob(int jobSize) {
        int blockSize = nextPowerOf2(jobSize);  // Find the smallest block that can fit the request
        
        // Find the smallest free block large enough to satisfy the request
        boolean allocated = false;
        for (int i = log2(blockSize); i < freeBlocks.length; i++) {
            if (freeBlocks[i] > 0) {
                // Allocate this block
                freeBlocks[i]--;
                int allocatedBlock = (int) Math.pow(2, i);
                
                // Split the block if necessary to match the job size
                while (i > log2(blockSize)) {
                    i--;
                    freeBlocks[i]++;
                }
                
                allocated = true;
                System.out.println("Allocated Block of size: " + allocatedBlock + " KB");
                break;
            }
        }
        
        if (!allocated) {
            System.out.println("Can't allocate due to insufficient memory.");
        }
        
        showCurrentBlocks(); // Show current memory state after allocation
    }

    // Function to show current blocks and their states (free or allocated)
    public static void showCurrentBlocks() {
        System.out.println("Current Memory Blocks:");
        for (int i = 0; i < freeBlocks.length; i++) {
            int blockSize = (int) Math.pow(2, i);
            if (freeBlocks[i] > 0) {
                System.out.println("Block Size: " + blockSize + " KB | Free Blocks: " + freeBlocks[i]);
            }
        }
    }

    // Function to check if a string input is a valid integer
    public static boolean isValidIntegerInput(String input) {
        try {
            Integer.parseInt(input); // Try parsing the input as an integer
            return true;
        } catch (NumberFormatException e) {
            return false; // Return false if input is not an integer
        }
    }

    // Main method to interact with the user
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize Buddy System with 1024KB of memory
        initializeBuddySystem(MAX_MEMORY);
        
        // Display initial memory state
        showCurrentBlocks();
        
        // Start interactive allocation process
        while (true) {
            System.out.print("Enter job size (in KB, or enter 0 to exit): ");
            String input = scanner.nextLine();
            
            if (input.equals("0")) {
                break; // Exit the program if user enters 0
            }
            
            // Check if the input is a valid integer
            if (isValidIntegerInput(input)) {
                int jobSize = Integer.parseInt(input);
                
                if (jobSize > 0) {
                    allocateJob(jobSize); // Allocate memory for the job
                } else {
                    System.out.println("Invalid job size. Must be a positive number.");
                }
            } else {
                System.out.println("Wrong input. Please enter a valid integer.");
            }
        }
        
        scanner.close();
    }
}
