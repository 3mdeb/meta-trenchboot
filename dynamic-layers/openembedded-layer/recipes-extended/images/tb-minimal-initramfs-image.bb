require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL_append = " \
  packagegroup-tb-base \
  packagegroup-tb-core \
  grub-tb-common \
"

IMAGE_FSTYPES += "cpio.gz"
QB_DEFAULT_FSTYPE = "${IMAGE_FSTYPES}"
IMAGE_FEATURES_append = " ssh-server-openssh"
