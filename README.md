# meta-trenchboot

[![pipeline status](https://gitlab.com/trenchboot1/3mdeb/meta-trenchboot/badges/master/pipeline.svg)](https://gitlab.com/trenchboot1/3mdeb/meta-trenchboot/-/commits/master)

Meta layer for the Trenchboot purposes

---

## WARNING

This is WIP repo and it is under development. Use it at your own risk.
If you have use-cases for such thing to be developed, please submit
an issue or PR with description of your needs / fixes.

After upgrade to Yocto honister build configurations from `meta-trenchboot` was
not tested and may need additional work (e.g adopting patches to new version of
sources).

---

## Prerequisites

* Linux PC (tested on `Fedora 39`)

* [docker](https://docs.docker.com/engine/install/fedora/) installed

* [kas-container 3.0.2](https://raw.githubusercontent.com/siemens/kas/3.0.2/kas-container)
  script downloaded and available in [PATH](https://en.wikipedia.org/wiki/PATH_(variable))

    ```bash
    wget -O ~/bin/kas-container https://raw.githubusercontent.com/siemens/kas/3.0.2/kas-container
    chmod +x ~/bin/kas-container
    ```

* `meta-trenchboot` repository cloned

    ```bash
    mkdir yocto
    cd yocto
    git clone https://github.com/3mdeb/meta-trenchboot.git
    ```

* [bmaptool](https://docs.yoctoproject.org/dev-manual/bmaptool.html) installed

    ```bash
    sudo dnf install bmap-tools
    ```

    > You can also use `bmap-tools`
    > [from github](https://github.com/yoctoproject/bmaptool) if it is not
    > available in your distro.

## Build

* From `yocto` directory run:

    ```shell
    kas-container build meta-trenchboot/kas-generic-tb.yml
    ```

* Image build takes time, so be patient and after build's finish you should see
something similar to (the exact tasks numbers may differ):

    ```shell
    Initialising tasks: 100% |###########################################################################################| Time: 0:00:01
    Sstate summary: Wanted 360 Found 0 Missed 8 Current 1810 (0% match, 99% complete)
    NOTE: Executing Tasks
    NOTE: Tasks Summary: Attempted 4774 tasks of which 4749 didn't need to be rerun and all succeeded.
    ```

Thanks to publishing the build cache on cache.dasharo.com the time needed to
finish the process should be significantly decreased.
Using the cache is enabled in kas/cache.yml file and can be disabled by removing
reference to this file in `kas/common.yml`:

```yaml
includes:
    - cache.yml
```

This cache can decrease time needed to build image from scratch from hours to
minutes depending on build machine and network connection.

```shell
Sstate summary: Wanted 2170 Local 0 Mirrors 2151 Missed 19 Current 0 (99% match, 0% complete)
NOTE: Executing Tasks
NOTE: Tasks Summary: Attempted 4774 tasks of which 4445 didn't need to be rerun and all succeeded.
```

## Flash

To flash resulting image:

* Find out your device name:

    ```shell
    $ lsblk
    NAME                                     MAJ:MIN RM   SIZE RO TYPE  MOUNTPOINTS
    sdx                                      179:0    0  14.8G  0 disk
    ├─sdx1                                   179:1    0     4M  0 part
    └─sdx2                                   179:2    0     4M  0 part
    ```

    In this case the device name is `/dev/sdx` **but be aware, in next steps
    replace `/dev/sdx` with right device name on your platform or else you can
    damage your system!.**

* From where you ran image build type:

    ```shell
    cd build/tmp/deploy/images/genericx86-64/
    sudo umount /dev/sdx*
    sudo bmaptool copy tb-minimal-image-genericx86-64.rootfs.wic.gz /dev/sdx
    ```

    and you should see output similar to this (the exact size number may differ):

    ```shell
    bmaptool: info: block map format version 2.0
    bmaptool: info: 275200 blocks of size 4096 (1.0 GiB), mapped 73240 blocks (286.1 MiB or 26.6%)
    bmaptool: info: copying image 'tb-minimal-image-genericx86-64.rootfs.wic.gz' to block device '/dev/sdx' using bmap file 'tb-minimal-image-genericx86-64.rootfs.wic.bmap'
    bmaptool: info: 100% copied
    bmaptool: info: synchronizing '/dev/sdx'
    bmaptool: info: copying time: 19.3s, copying speed 14.9 MiB/sec
    ```

## Booting

To run TrenchBoot connect drive with flashed image to target platform and boot
from it. In GRUB menu you can choose normal `boot` or `slaunch-boot`.

```text
                             GNU GRUB  version 2.06

 +----------------------------------------------------------------------------+
 |*boot                                                                       |
 | slaunch-boot                                                               |
 |                                                                            |
 |                                                                            |
 |                                                                            |
 +----------------------------------------------------------------------------+

      Use the ^ and v keys to select which entry is highlighted.
      Press enter to boot the selected OS, `e' to edit the commands
```

After a while you should see login prompt.

```text
early console in extract_kernel
input_data: 0x0000000006801548
input_len: 0x000000000121e953
output: 0x0000000004600000
output_len: 0x00000000033caee8
kernel_total_size: 0x0000000003030000
needed_size: 0x0000000003400000
trampoline_32bit: 0x0000000000000000
Physical KASLR using RDRAND RDTSC...
Virtual KASLR using RDRAND RDTSC...

Decompressing Linux... Parsing ELF... Performing relocations... done.
Booting the kernel (entry_offset: 0x0000000000000000).


Reference Yocto distro for PC Engines hardware 0.2.0 tb ttyS0

tb login:
```

To login use `root` account without password.

## Running in QEMU

It's possible to test image by running it in QEMU. Depending on QEMU
configuration not all features may be available, slaunch boot among others.

To create bootable image you can once again use `bmaptool` but this time instead
of writing image to flash device you write it to file

```shell
cd build/tmp/deploy/images/genericx86-64
bmaptool copy tb-minimal-image-genericx86-64.rootfs.wic.gz trenchboot.img
```

After that you can start QEMU

```shell
qemu-system-x86_64 -serial stdio -drive file=trenchboot.img,if=virtio -enable-kvm
```

## Funding

This project is partially funded through
[NGI0 Entrust](https://nlnet.nl/entrust), a fund established by
[NLnet](https://nlnet.nl) with financial support from the European Commission's
[Next Generation Internet](https://ngi.eu) program. Learn more at the
[NLnet project page](https://nlnet.nl/project/TrenchBoot-AMD/).

<p align="center">
    <a href="https://nlnet.nl">
        <img src="https://nlnet.nl/logo/banner.png"
             alt="NLnet foundation logo"
             height="75" />
    </a>
    <a href="https://nlnet.nl/entrust">
        <img src="https://nlnet.nl/image/logos/NGI0_tag.svg"
             alt="NGI Zero logo"
             height="75" />
    </a>
</p>
