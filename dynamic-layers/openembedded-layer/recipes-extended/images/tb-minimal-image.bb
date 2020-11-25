require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL_append = " \
  packagegroup-tb-base \
  packagegroup-tb-utils \
  grub-tb-common \
"

WKS_FILE_DEPENDS_remove = "grub-efi"

IMAGE_FSTYPES += "wic wic.gz wic.bmap"
IMAGE_FEATURES_append = " ssh-server-openssh"
do_image_wic[depends] += "landing-zone:do_deploy"
