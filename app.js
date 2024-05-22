import React, { useState, useEffect } from 'react';
import axios from 'axios';
import styled from 'styled-components';

const Container = styled.div`
    background-image: url('/back.jpg');
    background-size: cover;
    background-position: center;
    height: 100vh;
    width: 100vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    overflow: hidden;
    margin: 0;
    padding: 0;
    box-sizing: border-box;
`;

const HeaderContainer = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    background-color: transparent;
    padding: 10px;
    box-sizing: border-box;
    z-index: 1;
`;

const Title = styled.h1`
    font-size: 4rem;
    margin-bottom: 20px;
    color: #FFD700;
    text-shadow: 3px 3px 0px #000000;
`;

const Input = styled.input`
    padding: 15px 30px;
    margin-right: 10px;
    font-size: 24px;
    border: 2px solid #000;
    border-radius: 5px;
`;

const Button = styled.button`
    padding: 15px 30px;
    margin: 10px;
    background-color: #D2B48C;
    color: #fff;
    border: 2px solid #000;
    border-radius: 5px;
    cursor: pointer;
    font-size: 24px;
    text-transform: uppercase;
    text-shadow: 1px 1px 1px #000;
    width: 450px;
    text-align: center;
`;

const DishInfo = styled.div`
    text-align: center;
    color: #fff;
    background-color: #D2B48C;
    border-radius: 10px;
    padding: 20px;
    font-size: 36px;
    text-shadow: 1px 1px 1px #000;
    height: 200px;
    overflow: auto;
`;

const IngredientInfo = styled.div`
    text-align: center;
    color: #fff;
    background-color: #D2B48C;
    border-radius: 10px;
    padding: 20px;
    font-size: 36px;
    text-shadow: 1px 1px 1px #000;
`;

const SuccessMessage = styled.div`
    text-align: center;
    color: #fff;
    background-color: #D2B48C;
    border-radius: 10px;
    padding: 20px;
    font-size: 36px;
    text-shadow: 1px 1px 1px #000;
`;

const ErrorMessage = styled.div`
    color: red;
    margin-top: 10px;
`;

const HomeButton = styled.button`
    position: fixed;
    top: 10px;
    right: 10px;
    padding: 5px 10px;
    background-color: #8B0000   ;
    color: #fff;
    border: 2px solid #000;
    border-radius: 5px;
    cursor: pointer;
    font-size: 18px;
    text-transform: uppercase;
    text-shadow: 1px 1px 1px #000;
`;

const ButtonGroup = styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 20px;
`;

const UpdateButton = styled.button`
    background-color: #FFD700;
    color: #000;
    border: none;
    border-radius: 5px;
    padding: 5px 10px;
    font-size: 18px;
    margin-right: 10px;
    cursor: pointer;
`;

const DeleteButton = styled.button`
    background-color: #B22222;
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 5px 10px;
    font-size: 18px;
    cursor: pointer;
`;

const DishesListTitle = styled.h2`
    color: #FFD700;
    text-shadow: 1px 1px 0px #000000;
    margin-top: 20px;
    margin-bottom: 10px;
`;

function App() {
    const [step, setStep] = useState('select');
    const [name, setName] = useState('');
    const [country, setCountry] = useState('');
    const [category, setCategory] = useState('');
    const [instruction, setInstruction] = useState('');
    const [id, setId] = useState('');
    const [newName, setNewName] = useState('');
    const [newCountry, setNewCountry] = useState('');
    const [newCategory, setNewCategory] = useState('');
    const [newInstruction, setNewInstruction] = useState('');
    const [dish, setDish] = useState(null);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(null);
    const [ingredientId, setIngredientId] = useState('');
    const [dishes, setDishes] = useState([]);
    const [dishesWithIngredient, setDishesWithIngredient] = useState([]);
    const [ingredient, setIngredient] = useState(null);
    const [selectedDish, setSelectedDish] = useState(null);

    const resetState = () => {
        setStep('select');
        setName('');
        setCountry('');
        setCategory('');
        setInstruction('');
        setId('');
        setNewName('');
        setNewCountry('');
        setNewCategory('');
        setNewInstruction('');
        setDish(null);
        setSuccess(false);
        setError(null);
        setIngredientId('');
        setDishes([]);
        setDishesWithIngredient([]);
        setIngredient(null);
        setSelectedDish(null);
    };

    useEffect(() => {
        fetchDishes();
    }, []);

    const fetchDishes = async () => {
        try {
                const response = await axios.get(`http://localhost:8080/dishes/all`);
                console.log(response.data);
                setDishes(response.data);

        } catch (error) {
            console.error(error);
        }
    };

    const handleAddDish = async () => {
        try {
            const response = await axios.post(`http://localhost:8080/dishes`, { name, country, category, instruction});
            console.log(response.data);
            setStep('result');
            setSuccess(true);
            setError(null);

            setDishes(prevDishes => [...prevDishes, response.data]);
        } catch (error) {
            setError(error.response?.data || 'An error occurred');
        }
    };

    const handleUpdateDish = async () => {
        try {
            const response = await axios.put(`http://localhost:8080/dishes?name=${name}`, { name: newName, country: newCountry, category: newCategory, instruction: newInstruction});
            console.log(response.data);
            setStep('result');
            setSuccess(true);
            setError(null);
            setSelectedDish(null);
        } catch (error) {
            setError(error.response?.data || 'An error occurred');
        }
    };

    const handleDeleteDish = async () => {
        console.log(`Deleting dish with name: ${name}`);
        try {
            const response = await axios.delete(`http://localhost:8080/dishes`, { params: { name } });
            console.log(response.data);
            setDish(null);
            setSuccess(true);
            setError(null);
            setStep('result');

            setDishes(prevDishes => prevDishes.filter(dish => dish.id !== id));
            setSelectedDish(null);
        } catch (error) {
            console.log(`Error deleting dish: ${error.response?.data || 'An error occurred'}`);
            setError(error.response?.data || 'An error occurred');
        }
    };


    const handleGetIngredient = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/ingredients?id=${ingredientId}`);
            console.log(response.data);
            setIngredient(response.data);
            setStep('result');
            setError(null);
        } catch (error) {
            setError(error.response.data.message);
            setStep("result");
        }
    };

    const getDishesWithIngredient = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/dishes/with-ingredient?ingredientId=${ingredientId}`);
            console.log(response.data);
            setDishesWithIngredient(response.data);
            setStep('result');
            setError(null);
        } catch (error) {
            setError(error.response.data.message);
            setStep("result");
        }
    };

    const handleDishClick = (dish) => {
        setSelectedDish(dish);
        setStep('result');
    };

    const handleUpdateClick = (dish) => {
        setSelectedDish(dish);
        setName(dish.name);
        setCountry(dish.country);
        setCategory(dish.category);
        setInstruction(dish.instruction);
        setId(dish.id);
        setStep('update');
    };

    const handleDeleteClick = (dish) => {
        setSelectedDish(dish);
        setId(dish.id);
        setName(dish.name);
        console.log(`Preparing to delete dish with name: ${dish.name}`);
        setStep('delete');
    };


    const handleHomeClick = () => {
        resetState();
        fetchDishes();
    };

    return (
        <Container>
            <HeaderContainer>
                <Title>Dish Application</Title>
                <HomeButton onClick={handleHomeClick}>Home</HomeButton>
            </HeaderContainer>
            {step === 'select' && (
                <>
                    <ButtonGroup>
                        <Button onClick={() => setStep('add')}>Add Dish</Button>
                        <Button onClick={() => setStep('getIngredient')}>Get Ingredient</Button>
                        <Button onClick={() => setStep('getDishesWithIngredient')}>
                            Get Dishes With Ingredient
                        </Button>
                    </ButtonGroup>
                    <DishesListTitle>Dishes:</DishesListTitle>
                    <div>
                        {dishes.map((dish) => (
                            <div key={dish.id} style={{ display: 'flex', alignItems: 'center' }}>
                                <Button
                                    onClick={() => handleDishClick(dish)}
                                    style={{ marginRight: '10px', marginBottom: '10px' }}
                                >
                                    {dish.name}
                                </Button>
                                <UpdateButton
                                    onClick={() => handleUpdateClick(dish)}
                                    disabled={step !== 'select'}
                                    style={{ marginRight: '10px' }}
                                >
                                    Update
                                </UpdateButton>
                                <DeleteButton
                                    onClick={() => handleDeleteClick(dish)}
                                    disabled={step !== 'select'}
                                >
                                    Delete
                                </DeleteButton>
                            </div>
                        ))}
                    </div>
                </>
            )}

            {step === 'update' && (
                <>
                    {selectedDish && (
                        <>
                            <Input
                                type="text"
                                value={newName}
                                onChange={(e) => setNewName(e.target.value)}
                                placeholder="Enter new dish name"
                            />
                            <Input
                                type="text"
                                value={newCountry}
                                onChange={(e) => setNewCountry(e.target.value)}
                                placeholder="Enter new dish country"
                            />
                            <Input
                                type="text"
                                value={newCategory}
                                onChange={(e) => setNewCategory(e.target.value)}
                                placeholder="Enter new dish category"
                            />
                            <Input
                                type="text"
                                value={newInstruction}
                                onChange={(e) => setNewInstruction(e.target.value)}
                                placeholder="Enter new dish instruction"
                            />
                            <Button onClick={handleUpdateDish}>Update Dish</Button>
                        </>
                    )}
                </>
            )}

            {step === 'delete' && (
                <>
                    {selectedDish && (
                        <>
                            <p style={{
                                color: 'white',
                                fontSize: '36px',
                                textShadow: '2px 2px 0px black, -2px -2px 0px black, 2px -2px 0px black, -2px 2px 0px black'
                            }}>
                                Are you sure you want to delete the dish "{selectedDish.name}"?
                            </p>
                            <Button onClick={handleDeleteDish}>Delete Dish</Button>
                            <Button onClick={() => setStep('select')}>Cancel</Button>
                        </>
                    )}
                </>
            )}

            {step === 'add' && (
                <>
                <Input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Enter dish name"
                    />
                    <Input
                        type="text"
                        value={country}
                        onChange={(e) => setCountry(e.target.value)}
                        placeholder="Enter dish country"
                    />
                    <Input
                        type="text"
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        placeholder="Enter dish category"
                    />
                    <Input
                        type="text"
                        value={instruction}
                        onChange={(e) => setInstruction(e.target.value)}
                        placeholder="Enter dish instruction"
                    />
                    <Button onClick={handleAddDish}>Add Dish</Button>
                </>
            )}

            {step === 'getIngredient' && (
                <>
                    <Input
                        type="text"
                        value={ingredientId}
                        onChange={(e) => setIngredientId(e.target.value)}
                        placeholder="Enter ingredient ID"
                    />
                    <Button onClick={handleGetIngredient}>Get Ingredient</Button>
                </>
            )}

            {step === 'getDishesWithIngredient' && (
                <>
                    <Input
                        type="text"
                        value={ingredientId}
                        onChange={(e) => setIngredientId(e.target.value)}
                        placeholder="Enter ingredient ID"
                    />
                    <Button onClick={getDishesWithIngredient}>
                        Get Dishes With Ingredient
                    </Button>
                </>
            )}

            {step === 'result' && (
                <>
                    {success && <SuccessMessage>Dish was successfully processed!</SuccessMessage>}
                    {selectedDish && (
                        <DishInfo>
                            <div>Dish: {selectedDish.name}</div>
                            <div>Country: {selectedDish.country}</div>
                            <div>Category: {selectedDish.category}</div>
                            <div>Instruction: {selectedDish.instruction}</div>
                        </DishInfo>
                    )}
                    {ingredient && (
                            <IngredientInfo>
                                <div>Ingredient: {ingredient.name}</div>
                            </IngredientInfo>
                    )}
                    {dishesWithIngredient && dishesWithIngredient.map((dish, index) => (
                        <DishInfo key={index}>
                            <div>Dish: {dish.name}</div>
                            <div>Country: {dish.country}</div>
                            <div>Category: {dish.category}</div>
                            <div>Instruction: {dish.instruction}</div>
                        </DishInfo>
                    ))}
                    {error && <ErrorMessage>Error: {error}</ErrorMessage>}
                </>
            )}
        </Container>
    );
}

export default App;
