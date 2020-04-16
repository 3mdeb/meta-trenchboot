IMAGE_INSTALL_append = " \
  kernel-modules \
  xen-misc \
  tpm2-tools \
  tpm2-abrmd \
  tpm2-tss \
  libtss2 \
  libtss2-mu \
  libtss2-tcti-device \
  libtss2-tcti-mssim \
  grub \
  lvm2 \
  bridge-utils \
  openvswitch \
  dhcp-client \
  netcat \
  landing-zone \
  "

IMAGE_ROOTFS_EXTRA_SPACE = "2097152"

IMAGE_FSTYPES += "wic.gz wic.bmap"
