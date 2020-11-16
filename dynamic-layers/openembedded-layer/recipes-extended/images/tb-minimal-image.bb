require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL_append = " \
  kernel-modules \
  tpm2-tools \
  tpm2-abrmd \
  tpm2-tss \
  libtss2 \
  libtss2-mu \
  libtss2-tcti-device \
  libtss2-tcti-mssim \
  landing-zone \
  grub-tb-common \
  dhcp-client \
  cbmem \
"

WKS_FILE_DEPENDS_remove = "grub-efi"

IMAGE_FSTYPES += "wic wic.gz wic.bmap"
IMAGE_FEATURES_append = " ssh-server-openssh"
do_image_wic[depends] += "landing-zone:do_deploy"
