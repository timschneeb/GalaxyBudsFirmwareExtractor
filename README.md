# GalaxyBuds FirmwareExtractor

Utility to extract firmware update packages for Galaxy Buds devices into raw firmware binaries for use with disassemblers.

Sample output:
```
Analysing binary...

Firmware archive "FOTA_R175XXU0ATH7.bin" Magic=cafecafe TotalSize=1444388
│
├─┐  [Binary segments] SegmentCount=7
│ ├─ ID=1	Offset=0x007c	Size=180122	CRC32=0xf1ad0d6f
│ ├─ ID=6	Offset=0x2c016	Size=368740	CRC32=0xc98f567a
│ ├─ ID=7	Offset=0x8607a	Size=104516	CRC32=0x72b7396e
│ ├─ ID=10	Offset=0x9f8be	Size=154260	CRC32=0xf4f10cc2
│ ├─ ID=11	Offset=0xc5352	Size=58124	CRC32=0x391695ef
│ ├─ ID=12	Offset=0xd365e	Size=147232	CRC32=0xeffbc7ed
│ └─ ID=20	Offset=0xf757e	Size=431266	CRC32=0x2144df1c
│
├─┐  [Audio segments]
│ ├─ ID=0	Offset=0x1009bb	Size=16896	Bitrate=128000	Samplerate=48000
│ ├─ ID=1	Offset=0x1055c9	Size=16896	Bitrate=128000	Samplerate=48000
│ ├─ ID=2	Offset=0x109869	Size=12672	Bitrate=128000	Samplerate=48000
│ ├─ ID=3	Offset=0x10ca09	Size=3264	Bitrate=32000	Samplerate=48000
│ ├─ ID=4	Offset=0x10d6e9	Size=4320	Bitrate=32000	Samplerate=48000
│ ├─ ID=5	Offset=0x10e7e9	Size=10056	Bitrate=32000	Samplerate=48000
│ ├─ ID=6	Offset=0x110f51	Size=1584	Bitrate=32000	Samplerate=48000
│ ├─ ID=7	Offset=0x1115a1	Size=5880	Bitrate=128000	Samplerate=48000
│ ├─ ID=8	Offset=0x112cb9	Size=4680	Bitrate=32000	Samplerate=48000
│ ├─ ID=9	Offset=0x113f21	Size=3888	Bitrate=32000	Samplerate=48000
│ ├─ ID=10	Offset=0x114e71	Size=2496	Bitrate=32000	Samplerate=48000
│ ├─ ID=11	Offset=0x115851	Size=17496	Bitrate=32000	Samplerate=48000
│ ├─ ID=12	Offset=0x119cc9	Size=7920	Bitrate=32000	Samplerate=48000
│ ├─ ID=13	Offset=0x11bbd9	Size=6552	Bitrate=32000	Samplerate=48000
│ ├─ ID=14	Offset=0x11d591	Size=7872	Bitrate=32000	Samplerate=48000
│ ├─ ID=15	Offset=0x11f471	Size=14208	Bitrate=32000	Samplerate=48000
│ ├─ ID=16	Offset=0x122c11	Size=3240	Bitrate=32000	Samplerate=48000
│ ├─ ID=17	Offset=0x1238d9	Size=16704	Bitrate=192000	Samplerate=48000
│ ├─ ID=18	Offset=0x127a39	Size=19008	Bitrate=192000	Samplerate=48000
│ ├─ ID=19	Offset=0x12c499	Size=23040	Bitrate=192000	Samplerate=48000
│ ├─ ID=20	Offset=0x131eb9	Size=8424	Bitrate=32000	Samplerate=48000
│ ├─ ID=21	Offset=0x134a93	Size=20480	Bitrate=128000	Samplerate=44100
│ ├─ ID=22	Offset=0x139b33	Size=3216	Bitrate=32000	Samplerate=48000
│ ├─ ID=23	Offset=0x13a7e3	Size=3048	Bitrate=32000	Samplerate=48000
│ ├─ ID=24	Offset=0x13b3eb	Size=3120	Bitrate=32000	Samplerate=48000
│ ├─ ID=25	Offset=0x13c03b	Size=3024	Bitrate=32000	Samplerate=48000
│ ├─ ID=26	Offset=0x13cc2b	Size=5064	Bitrate=32000	Samplerate=48000
│ └─ [EOF] SegmentCount=27

Extracting binary segments into raw firmware image... Done
Extracting audio segments as MP3 files... Done

Data has been written to '/home/tim/FOTA_R175XXU0ATH7_out'

```
