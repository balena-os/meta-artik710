require build-artik.inc

DESCRIPTION = "Prebuilt tools for generating a single bootloader image - Samsung boards"
SECTION = "bootloaders"
LICENSE = "GPLv2"

inherit native

TOOLS = "fip_create BOOT_BINGEN"

do_install () {
    install -d ${D}/${sbindir}

    for file in ${TOOLS}; do
        install -m 755 ${S}/prebuilt/$file ${D}/${sbindir}
    done

    install -m 755 ${S}/gen_singleimage.sh ${D}/${sbindir}
}
