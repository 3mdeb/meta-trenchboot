require conf/distro/poky.conf

DISTRO_NAME = "Reference Yocto distro for PC Engines hardware"
DISTRO_VERSION = "0.1"
SDK_VENDOR = "-tbsdk"
TARGET_VENDOR = "-tb"

MAINTAINER = "maciej.pijanowski@3mdeb.com"

DISTRO_FEATURES = " \
    ext2 \
    keyboard \
    largefile \
    ldconfig \
    nfs \
    pci \
    systemd \
    usbgadget \
    usbhost \
    tpm2 \
"

# disable sysvinit
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = ""
VIRTUAL-RUNTIME_syslog = ""
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"

PREFERRED_PROVIDER_virtual/kernel = "linux-tb"
PREFERRED_VERSION_linux-tb ?= "5.5%"
