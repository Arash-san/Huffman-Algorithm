# Huffman-Algorithm
This repository contains an implementation of the Huffman algorithm, a lossless data compression technique. The algorithm works by assigning variable-length codes to symbols based on their frequencies of occurrence in the input data. The implementation includes functions to construct the frequency table and binary tree that represent the set of prefix-free codes that will be used to compress the data, as well as functions to encode and decode the input data using the Huffman codes. The resulting compressed data can be stored or transmitted more efficiently than the original data, and the implementation ensures that the data can be accurately decompressed back into its original form.

There is an example file called alice.txt ([source](https://corpus.canterbury.ac.nz/descriptions/#cantrbry)) which can be used for testing this algorithm. As shown in the screenshot, the compressed file is about 65% of the original file size.

Please note that this implementation is designed for educational purposes and is not optimized for large files.


## Screenshots

<img src="https://github.com/Arash-san/Huffman-Algorithm/blob/main/screenshot.jpg?raw=true" width="900">
