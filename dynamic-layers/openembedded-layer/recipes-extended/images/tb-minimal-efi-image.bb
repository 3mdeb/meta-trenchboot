require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL:append = " \
  packagegroup-tb-base \
  packagegroup-tb-core \
  packagegroup-tb-virtualization \
  grub-efi \
"

IMAGE_FSTYPES += "wic.gz wic.bmap"
IMAGE_FEATURES:append = " ssh-server-openssh"
