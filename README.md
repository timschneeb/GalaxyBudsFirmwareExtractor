# GalaxyBuds FirmwareExtractor

Utility to extract firmware update packages for Galaxy Buds devices into raw firmware binaries for use with disassemblers.

Sample output:
```
Analysing binary...

[FW Binary] └─┐ "FOTA_R170XXU0ATD3.bin" Magic=cafecafe TotalSize=1461534 EntryCount=7
[FW Entry]    ├─ ID=1 CRC32=0xae08fd4b Position=124 Size=187794
[FW Entry]    ├─ ID=6 CRC32=0x601a8f15 Position=187918 Size=265092
[FW Entry]    ├─ ID=7 CRC32=0x2369e8dd Position=453010 Size=82324
[FW Entry]    ├─ ID=10 CRC32=0xdabc8347 Position=535334 Size=126756
[FW Entry]    ├─ ID=11 CRC32=0x8a562b18 Position=662090 Size=42332
[FW Entry]    ├─ ID=12 CRC32=0xac4daa1c Position=704422 Size=113620
[FW Entry]    └─ ID=20 CRC32=0x2144df1c Position=818042 Size=643488

Extracting into raw firmware image...

[FW Binary] Extracting segment #1 from offset 0x7c to 0x2de0e
[FW Binary] Extracting segment #6 from offset 0x2de0e to 0x6e992
[FW Binary] Extracting segment #7 from offset 0x6e992 to 0x82b26
[FW Binary] Extracting segment #10 from offset 0x82b26 to 0xa1a4a
[FW Binary] Extracting segment #11 from offset 0xa1a4a to 0xabfa6
[FW Binary] Extracting segment #12 from offset 0xabfa6 to 0xc7b7a
[FW Binary] Extracting segment #20 from offset 0xc7b7a to 0x164d1a

Raw firmware image has been extracted to '/home/tim/FOTA_R170XXU0ATD3.raw.bin'.
You can view it in a disassembler.

```
