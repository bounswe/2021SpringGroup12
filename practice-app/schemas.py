from typing import List, Optional
from pydantic import BaseModel


class Cocktail(BaseModel):
    cocktail_name: str
    ingredient_1: str
    ingredient_2: str
    ingredient_3: str
    ingredient_4: str
    ingredient_4: str
    glass: str
    instructions: str
    
class CocktailResponse(BaseModel):
    cocktails: List[dict]

class ErrorResponse(BaseModel):
    message: Optional[str] =""

