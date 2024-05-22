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

* [docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/) installed

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

* [bmaptool](https://source.tizen.org/documentation/reference/bmaptool) installed

```bash
sudo dnf install bmap-tools
```

> You can also use `bmap-tools`
> [from github](https://github.com/intel/bmap-tools) if it is not available in
> your distro.

## Build

* From `yocto` directory run:

```shell
$ SHELL=/bin/bash kas-container build meta-trenchboot/kas-generic-tb.yml
```

* Image build takes time, so be patient and after build's finish you should see
something similar to (the exact tasks numbers may differ):

```shell
Initialising tasks: 100% |###########################################################################################| Time: 0:00:01
Sstate summary: Wanted 2 Found 0 Missed 2 Current 931 (0% match, 99% complete)
NOTE: Executing Tasks
NOTE: Tasks Summary: Attempted 2532 tasks of which 2524 didn't need to be rerun and all succeeded.
```

## Flash

This section assumes that image can be flashed on SD card.

* Find out your device name:

    ```shell
    $ lsblk
    NAME                                          MAJ:MIN RM   SIZE RO TYPE  MOUNTPOINTS
    mmcblk0                                       179:0    0  14.8G  0 disk
    ├─mmcblk0p1                                   179:1    0     4M  0 part
    └─mmcblk0p2                                   179:2    0     4M  0 part
    ```

    In this case the device name is `/dev/mmcblk0` **but be aware, in next steps
    replace `/dev/mmcblk0` with right device name on your platform or else you can
    damage your system!.**

* From where you ran image build type:

    ```shell
    $ cd build/tmp/deploy/images/genericx86-64/
    $ sudo umount /dev/mmcblk0*
    $ sudo bmaptool copy --bmap tb-minimal-image-genericx86-64.rootfs.wic.bmap.wic.bmap \
        tb-minimal-image-genericx86-64.rootfs.wic.bmap.wic.gz /dev/mmcblk0
    ```

    and you should see output similar to this (the exact size number may differ):

    ```shell
    bmaptool: info: block map format version 2.0
    bmaptool: info: 275200 blocks of size 4096 (1.0 GiB), mapped 73240 blocks (286.1 MiB or 26.6%)
    bmaptool: info: copying image 'tb-minimal-image-genericx86-64.rootfs.wic.gz' to block device '/dev/mmcblk0' using bmap file 'tb-minimal-image-genericx86-64.rootfs.wic.bmap'
    bmaptool: info: 100% copied
    bmaptool: info: synchronizing '/dev/mmcblk0'
    bmaptool: info: copying time: 19.3s, copying speed 14.9 MiB/sec
    ```

* Boot the platform
