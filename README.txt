CSC172Project1
UR CSC 172 Project #1
Nicholas Krein / Benjamin Levy


Order of tasks:
Encryption:
 Read the file (plaintext)
 Convert to binary 
 Encrypt the data (using given algorithm and user secret key)
 Write encrypted binary to file

Decryption:
 Read the file (binary)
 Decrypt the data (using given algorithm and user secret key)
 Convert to plaintext characters
 Write decrypted plaintext to a new file

Instructions:
When run, the program will first output test cases.
When prompted, answer E/D for encryption/Decryption, filename, and 56-bit binary key.
The program will encrypt/decrypt your file and write to a new file (either [name].encrypted or [name].decrypted respectively)

Features:
Read text from file, convert to binary
Separates binary into 64-bit blocks for encryption
Encrypts binary block by block
Writes encrypted text to file

Reads encrypted binary from file
Decrypts binary block by block
Outputs decrypted block with same formatting as original

Well encapsulated so that individual function methods cannot be accessed from outside the package

The program has methods for encrypting and decrypting individual blocks, writing the
encrypted or decrypted blocks to a file, converting text to binary and back, storing the input as 64-bit blocks, transforming
the secret key before each iteration of the function, each individual part of the round function process, and the round function itself.
The program includes each method listed under 1.3 (Required Methods).




Submission Requirements:
Zip (archive) all the project source files and a README file and save it as a Project1LastName.zip file. Include your LastName (+partner) in the filename. 
Upload the file to the appropriate folder on Gradescope. Your README file should include name of the members of the team
and any specific instruction which is useful for the project. It should also include all the features (including additional features) 
that you have implemented. Make sure all your source files are properly commented so that user can browse your code without getting lost.
