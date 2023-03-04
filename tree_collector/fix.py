from os import listdir, rename

species = [
    # "aesculus_californica",
    # "fraxinus_americana",
    # "gymnocladus_dioicus",
    # "larix_laricina",
    # "sorbus_americana",
    # "nyssa_sylvatica",
    "pacific_madrone",
    "picea_engelmannii",
    "picea_glauca",
    "picea_mariana",
    "picea_pungens",
    "picea_rubens",
    "picea_sitchensis",
    "pinus_albicaulis",
    "pinus_monticola",
    "prunus_americana",
    "prunus_pensylvanica",
    "quercus_imbricaria",
    "taxodium_distichum",
    "thuja_plicata",
    "tilia_americana",
    "ulmus_americana",
    "umbellularia_californica"
]

dir = "../app/src/main/res/drawable/"

for specie in species:
    count = 0

    for filename in listdir(dir):
        if "_".join(filename.split("_")[:-1]) in specie:
            new_filename = "_".join(filename.split("_")[:-1]) + f"_{count}"

            new_filename = new_filename.replace("pacific_madrone", "arbutus_menziesii").replace(".jpg", "")

            try:
                rename(f"{dir}{filename}", f"{dir}{new_filename}.jpg")
            except FileExistsError:
                print(f"{dir}{filename}")

            count += 1
