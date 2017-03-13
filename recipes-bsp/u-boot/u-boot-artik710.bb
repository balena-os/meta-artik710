# Samsung Artik 710 u-boot

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "dtc-native mksinglebootloader-tools secure-boot-artik710"

DESCRIPTION = "u-boot which includes support for the Samsung Artik boards."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PROVIDES += "u-boot"

SRCREV_artik710 = "8a9ae34f41a0c992344aa2dd1b1cc40d01161305"
SRC_URI_artik710 = " \
    git://git@github.com/resin-os/uboot-artik7.git;protocol=ssh \
    file://0001-artik710_raptor.h-Set-CONFIG_ROOT_PART-to-2.patch \
    file://0002-compiler-gcc6.h-Add-support-for-GCC6.patch \
    file://0003-artik710_raptor.h-Boot-partition-is-a-fat-one.patch \
    "

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(artik710)"

# Setup uboot environment for each flasher/non-flasher images
do_compile_append() {
    cp `find . -name "env_common.o"` copy_env_common.o
    ${OBJCOPY} -O binary --only-section=.rodata.default_environment `find . -name "copy_env_common.o"`
    tr '\0' '\n' < copy_env_common.o | grep '=' | tee default_envs_emmc.txt default_envs_sd.txt > /dev/null

    # root device should be (mmcblk)1 when booting the SD card flasher image (exit with code 1 when no replacement done so we error out when upstream modifies the u-boot env)
    sed -i '/rootdev=[0-9]\{1\}/{s//rootdev=1/;h};${x;/./{x;q0};x;q1}' default_envs_sd.txt

    # eMMC and SD card boots will default to "run mmcboot"
    sed -i "s/^bootcmd=.*/bootcmd=run mmcboot/" default_envs_emmc.txt
    sed -i "s/^bootcmd=.*/bootcmd=run mmcboot/" default_envs_sd.txt

    tools/mkenvimage -s 16384 -o params_emmc.bin default_envs_emmc.txt
    tools/mkenvimage -s 16384 -o params_sd.bin default_envs_sd.txt

    # generate FIP (Firmware Image Package) (fip-nonsecure.bin) from the uboot binary
    tools/fip_create/fip_create --dump --bl33 u-boot.bin fip-nonsecure.bin
    # generate nexell image (fip-nonsecure.img) from the FIP binary
    tools/nexell/SECURE_BINGEN -c ${BASE_MACH} -t 3rdboot -n ${S}/tools/nexell/nsih/raptor-64.txt -i ${B}/fip-nonsecure.bin -o ${B}/fip-nonsecure.img -l ${FIP_LOAD_ADDR} -e 0x00000000
}

do_singlebootloader() {
    if [ "${BOOTLOADER_SINGLEIMAGE}" = "1" ]; then
        bbdebug 1 "Creating single bootloader image..."
        fip_create --dump --bl2 ${DEPLOY_DIR_IMAGE}/${BL2_BIN} --bl31 ${DEPLOY_DIR_IMAGE}/${BL31_BIN} --bl32 ${DEPLOY_DIR_IMAGE}/${BL32_BIN} --bl33 ${B}/u-boot.bin ${B}/fip.bin
        gen_singleimage.sh -o ${B} -e ${DEPLOY_DIR_IMAGE}/${LLOADER_BIN} -f ${B}/fip.bin

        echo "BOOT_BINGEN -c ${BASE_MACH} -t 3rdboot -n ${NSIH_EMMC} -i ${B}/singleimage.bin -o ${B}/singleimage-emmcboot.bin -l ${BL2_LOAD_ADDR} -e ${BL2_JUMP_ADDR}"
        BOOT_BINGEN -c ${BASE_MACH} -t 3rdboot -n ${DEPLOY_DIR_IMAGE}/${NSIH_EMMC} -i ${B}/singleimage.bin -o ${B}/singleimage-emmcboot.bin -l ${BL2_LOAD_ADDR} -e ${BL2_JUMP_ADDR}
        BOOT_BINGEN -c ${BASE_MACH} -t 3rdboot -n ${DEPLOY_DIR_IMAGE}/${NSIH_SD}   -i ${B}/singleimage.bin -o ${B}/singleimage-sdboot.bin   -l ${BL2_LOAD_ADDR} -e ${BL2_JUMP_ADDR}
    else
        bbdebug 1 "Creating single bootloader image not requested through machine configuration."
    fi
}

do_deploy_append () {
    install ${B}/params_emmc.bin ${B}/params_sd.bin ${B}/fip-nonsecure.img ${DEPLOYDIR}
    if [ "${BOOTLOADER_SINGLEIMAGE}" = "1" ]; then
        install ${B}/singleimage-emmcboot.bin ${B}/singleimage-sdboot.bin ${DEPLOYDIR}
    fi
}

addtask singlebootloader before do_deploy after do_compile
