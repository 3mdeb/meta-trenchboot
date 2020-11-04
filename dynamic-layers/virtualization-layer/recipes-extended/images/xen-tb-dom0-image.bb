require recipes-extended/images/xen-image-minimal.bb

IMAGE_INSTALL_append = " \
  kernel-modules \
  coreutils \
  xen-misc \
  tpm2-tools \
  tpm2-abrmd \
  tpm2-tss \
  libtss2 \
  libtss2-mu \
  libtss2-tcti-device \
  libtss2-tcti-mssim \
  landing-zone \
  grub-tb-common \
  bridge-utils \
  seabios \
  dhcp-client \
  initramfs-tools \
  cryptsetup \
  lvm2 \
  e2fsprogs \
"

WKS_FILE_DEPENDS_remove = "grub-efi"

IMAGE_ROOTFS_EXTRA_SPACE = "2097152"
IMAGE_FSTYPES += "wic wic.gz wic.bmap"
do_image_wic[depends] += "landing-zone:do_deploy core-image-minimal-initramfs:do_image_complete"

build_syslinux_cfg () {
        echo "ALLOWOPTIONS 1" > ${SYSLINUX_CFG}
        echo "DEFAULT boot" >> ${SYSLINUX_CFG}
        echo "TIMEOUT 10" >> ${SYSLINUX_CFG}
        echo "PROMPT 1" >> ${SYSLINUX_CFG}
        echo "LABEL boot" >> ${SYSLINUX_CFG}
        echo "  KERNEL mboot.c32" >> ${SYSLINUX_CFG}
        echo "  APPEND /xen.gz dom0_mem=1024M ${SYSLINUX_XEN_ARGS} --- /${KERNEL_IMAGETYPE} ${SYSLINUX_KERNEL_ARGS} --- /initrd" >> ${SYSLINUX_CFG}
}
