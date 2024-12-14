import java.util.Scanner;

public class BuddySystem {

    private static final int MAX_MEMORY = 1024;  // Total memory in KB (1024 KB)
    private int[] freeBlocks;                    // Array to track free blocks of each power-of-2 size
    private int maxSize;                         // Largest block size
    private int maxBlocks;                       // Number of block sizes (log2 of maxSize + 1)

    // Constructor to initialize the buddy system
    public BuddySystem(int totalMemory) {
        maxSize = nextPowerOf2(totalMemory);  // Get the next power of 2 greater than or equal to totalMemory
        maxBlocks = (int) (Math.log(maxSize) / Math.log(2)) + 1;
        freeBlocks = new int[maxBlocks];
        
        // Initially, all the memory is available in one block of the largest size
        freeBlocks[maxBlocks - 1] = 1;
    }

    // Find the next power of 2 greater than or equal to the given size (in KB)
    private int nextPowerOf2(int size) {
        return (int) Math.pow(2, Math.ceil(Math.log(size) / Math.log(2)));
    }

    // Function to allocate memory (called when a job request is made)
    public void allocateJob(int jobSize) {
        int blockSize = nextPowerOf2(jobSize);  // Find the smallest block that fits the request
        System.out.println("Processing request for " + jobSize + " KB...");

        // Check if there is no free block large enough to accommodate the job size
        boolean allocated = false;
        for (int i = (int) (Math.log(blockSize) / Math.log(2)); i < maxBlocks; i++) {
            if (freeBlocks[i] > 0) {
                // Allocate this block
                freeBlocks[i]--;
                int allocatedBlock = (int) Math.pow(2, i);
                System.out.println("Allocated Block of size: " + allocatedBlock + " KB");

                // If the block is larger than needed, split it into smaller blocks
                while (i > (Math.log(blockSize) / Math.log(2))) {
                    i--;
                    freeBlocks[i]++;
                }

                allocated = true;
                break;
            }
        }

        // If no suitable block is found
        if (!allocated) {
            System.out.println("Can't allocate low memory. No suitable block found.");
        } else {
            showCurrentBlocks();
        }
    }

    // Function to print the current memory state: block sizes and whether free or allocated
    public void showCurrentBlocks() {
        System.out.println("\nCurrent Memory State:");
        for (int i = 0; i < maxBlocks; i++) {
            if (freeBlocks[i] > 0) {
                System.out.println(freeBlocks[i] + " block(s) of size " + (int) Math.pow(2, i) + " KB (free)");
            } else {
                System.out.println("0 block(s) of size " + (int) Math.pow(2, i) + " KB (allocated)");
            }
        }
    }

    // Main method to demonstrate Buddy System operations interactively
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BuddySystem buddySystem = new BuddySystem(MAX_MEMORY);

        System.out.println("Initial Memory Size: 1024 KB");

        // Infinite loop to handle user input continuously
        while (true) {
            System.out.print("\nEnter job size (in KB, or 0 to exit): ");
            String input = scanner.nextLine();

            // Check if the input is a valid integer
            try {
                int jobSize = Integer.parseInt(input);
                if (jobSize == 0) {
                    break;  // Exit the loop if the user inputs 0
                }

                // Call the allocateJob function to process the allocation request
                buddySystem.allocateJob(jobSize);

            } catch (NumberFormatException e) {
                // Print error if the input is not a valid integer
                System.out.println("Wrong input. Please enter a valid integer.");
            }
        }

        scanner.close();
    }
}
