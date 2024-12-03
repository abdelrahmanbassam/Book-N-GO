import { Button } from "./Button";
import {useEffect, useState} from "react";

export const SearchBar = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [searchCategory, setSearchCategory] = useState('all');
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await fetch('https://backend-api.com/categories');
                const data = await response.json();
                setCategories(data);
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        };

        fetchCategories().then(r => console.log(r));
    }, []);
    const handleSearch = async () => {
        try {
            const response = await fetch(`https://backend-api.com/search?term=${searchTerm}&category=${searchCategory}`);
            const data = await response.json();
            console.log(data);
            // Handle the response data as needed
        } catch (error) {
            console.error('Error fetching search results:', error);
        }
    };

    return (
        <>
            <div className={"flex w-full"}>
                <select
                    value={searchCategory}
                    onChange={(e) => setSearchCategory(e.target.value)}
                    className={"px-4 py-2 rounded-l-sm text-primary bg-secondary2 w-[150px] h-[40px]"}
                >
                    <option value="all">All</option>
                    {categories.map((category) => (
                        <option key={category.id} value={category.id}>{category.name}</option>
                    ))}
                </select>
                <input
                    type="text"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    placeholder=""
                    className={"px-4 py-2 w-full"}
                />
                <Button onClick={handleSearch} className={"rounded-r-sm"}>Search</Button>
            </div>
        </>
    );
};