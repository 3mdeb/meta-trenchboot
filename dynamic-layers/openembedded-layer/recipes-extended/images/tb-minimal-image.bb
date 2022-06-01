require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL:append = " \
  packagegroup-tb-base \
  packagegroup-tb-core \
  grub-tb-common \
"

WKS_FILE_DEPENDS:remove = "grub-efi"

IMAGE_FSTYPES += "wic wic.gz wic.bmap"
IMAGE_FEATURES:append = " ssh-server-openssh"
do_image_wic[depends] += "skl:do_deploy"
