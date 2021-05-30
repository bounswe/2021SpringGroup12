from schemas import Cocktail

def cocktail_mapper(feature: dict) -> Cocktail:
    return Cocktail(
        cocktail_name=feature["strDrink"],
        ingredient_1=feature["strIngredient1"] if feature["strIngredient1"] != None else "",
        ingredient_2=feature["strIngredient2"] if feature["strIngredient2"] != None else "",
        ingredient_3=feature["strIngredient3"] if feature["strIngredient3"] != None else "",        
        ingredient_4=feature["strIngredient4"] if feature["strIngredient4"] != None else "", 
        glass=feature["strGlass"],
        instructions=feature["strInstructions"], 
    )