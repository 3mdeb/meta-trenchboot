require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL:append = " \
  packagegroup-tb-base \
  packagegroup-tb-core \
  grub-tb-common \
"

IMAGE_FSTYPES += "cpio.gz"
QB_DEFAULT_FSTYPE = "${IMAGE_FSTYPES}"
IMAGE_FEATURES:append = " ssh-server-openssh"
