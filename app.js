import React, { useState } from 'react';
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

const ContentContainer = styled.div`
    margin-top: 60px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    border-radius: 10px;
    background-color: #00AAA9;
    padding: 20px;
    width: 50%;
    border: 2px solid black;
    box-sizing: border-box;
`;

const Title = styled.h1`
    font-size: 4rem;
    margin-bottom: 20px;
    color: #fff;
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: center;
    margin-top: 50px;
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
    background-color: #00AAA9;
    color: #fff;
    border: 2px solid #000;
    border-radius: 5px;
    cursor: pointer;
    font-size: 24px;
    text-transform: uppercase;
    text-shadow: 1px 1px 1px #000;
`;

const DishInfo = styled.div`
    text-align: center;
    color: #fff;
    background-color: #00AAA9;
    border-radius: 10px;
    padding: 20px;
    font-size: 36px;
    text-shadow: 1px 1px 1px #000;
`;

const IngredientInfo = styled.div`
    text-align: center;
    color: #fff;
    background-color: #00AAA9;
    border-radius: 10px;
    padding: 20px;
    font-size: 36px;
    text-shadow: 1px 1px 1px #000;
`;

const SuccessMessage = styled.div`
    text-align: center;
    color: #fff;
    background-color: #00AAA9;
    border-radius: 10px;
    padding: 20px;
    font-size: 36px;
    text-shadow: 1px 1px 1px #000;
`;

const ErrorMessage = styled.div`
    color: red;
    margin-top: 10px;
`;

const StartOverButton = styled(Button)`
    position: fixed;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    background-color: #800000;
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
    const [ingredient, setIngredient] = useState(null);

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
        setIngredient(null);
    };

    const handleAddDish = async () => {
        try {
            const response = await axios.post('http://localhost:8080/dishes', { name, country, category, instruction});
            console.log(response.data);
            setStep('result');
            setSuccess(true);
            setError(null);
        } catch (error) {
            setError(error.response?.data || 'An error occurred');
        }
    };


    const handleGetDish = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/dishes?name=${name}`);
            console.log(response.data);
            setDish(response.data);
            setStep('result');
            //setDish(response.data);
            setError(null);
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
        } catch (error) {
            setError(error.response?.data || 'An error occurred');
        }
    };

    const handleDeleteDish = async () => {
        try {
            const response = await axios.delete(`http://localhost:8080/dishes`, { params: { id } });
            console.log(response.data);
            setDish(null);
            setSuccess(true);
            setError(null);
            setStep('result');
        } catch (error) {
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
            setError(error.response?.data || 'An error occurred');
        }
    };

    const getDishesWithIngredient = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/dishes/with-ingredient?ingredientId=${ingredientId}`);
            console.log(response.data);
            setDishes(response.data);
            setStep('result');
            setError(null);
        } catch (error) {
            setError(error.response?.data || 'An error occurred');
        }
    };


        return (
            <Container>
                <HeaderContainer>
                    <Title>Dish Application</Title>
                </HeaderContainer>
                {step === 'select' && (
                    <>
                        <ButtonContainer>
                            <Button onClick={() => setStep('add')}>Add Dish</Button>
                            <Button onClick={() => setStep('get')}>Get Dish</Button>
                            <Button onClick={() => setStep('update')}>Update Dish</Button>
                            <Button onClick={() => setStep('delete')}>Delete Dish</Button>
                            <Button onClick={() => setStep('getIngredient')}>Get Ingredient</Button>
                            <Button onClick={() => setStep('getDishesWithIngredient')}>Get Dishes With Ingredient</Button>
                        </ButtonContainer>
                    </>
                )}
                {step === 'add' && (
                    <>
                        <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter dish name" />
                        <Input type="text" value={country} onChange={(e) => setCountry(e.target.value)} placeholder="Enter dish country" />
                        <Input type="text" value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Enter dish category" />
                        <Input type="text" value={instruction} onChange={(e) => setInstruction(e.target.value)} placeholder="Enter dish instruction" />
                        <Button onClick={handleAddDish}>Add Dish</Button>
                    </>
                )}
                {step === 'get' && (
                    <>
                        <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter dish name" />
                        <Button onClick={handleGetDish}>Get Dish</Button>
                    </>
                )}
                {step === 'update' && (
                    <>
                        <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter dish name" />
                        <Input type="text" value={newName} onChange={(e) => setNewName(e.target.value)} placeholder="Enter new dish name" />
                        <Input type="text" value={newCountry} onChange={(e) => setNewCountry(e.target.value)} placeholder="Enter new dish country" />
                        <Input type="text" value={newCategory} onChange={(e) => setNewCategory(e.target.value)} placeholder="Enter new dish category" />
                        <Input type="text" value={newInstruction} onChange={(e) => setNewInstruction(e.target.value)} placeholder="Enter new dish instruction" />
                        <Button onClick={handleUpdateDish}>Update Dish</Button>
                    </>
                )}
                {step === 'delete' && (
                    <>
                        <Input type="text" value={id} onChange={(e) => setId(e.target.value)} placeholder="Enter dish name" />
                        <Button onClick={handleDeleteDish}>Delete Dish</Button>
                    </>
                )}
                {step === 'getIngredient' && (
                    <>
                        <Input type="text" value={ingredientId} onChange={(e) => setIngredientId(e.target.value)} placeholder="Enter ingredient ID" />
                        <Button onClick={handleGetIngredient}>Get Ingredient</Button>
                    </>
                )}
                {step === 'getDishesWithIngredient' && (
                    <>
                        <Input type="text" value={ingredientId} onChange={(e) => setIngredientId(e.target.value)} placeholder="Enter ingredient ID" />
                        <Button onClick={getDishesWithIngredient}>Get Dishes With Ingredient</Button>
                    </>
                )}
                {step === 'result' && (
                    <ContentContainer>
                        {success && <SuccessMessage>Dish was successfully processed!</SuccessMessage>}
                        {dish && (
                            <DishInfo>
                                <div>Dish: {dish.name}</div>
                                <div>Country: {dish.country}</div>
                                <div>Category: {dish.category}</div>
                                <div>Instruction: {dish.instruction}</div>
                            </DishInfo>
                        )}
                        {dishes && dishes.map((dish, index) => (
                            <DishInfo key={index}>
                                <div>Dish: {dish.name}</div>
                                <div>Country: {dish.country}</div>
                                <div>Category: {dish.category}</div>
                                <div>Instruction: {dish.instruction}</div>
                            </DishInfo>
                        ))}
                        {ingredient && (
                            <IngredientInfo>
                                <div>Ingredient: {ingredient.name}</div>
                            </IngredientInfo>

                        )}
                        {error && <ErrorMessage>Error: {error}</ErrorMessage>}
                    </ContentContainer>
                )}
                <StartOverButton onClick={resetState}>Start Over</StartOverButton>
            </Container>
        );
}

export default App;
