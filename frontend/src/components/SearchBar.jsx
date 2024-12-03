import { colors } from "./styles";
import { Button } from "./Button";
import {useEffect, useState} from "react";

export const SearchBar = (props) => {
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

        fetchCategories();
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
                    style={{
                        backgroundColor: colors.secondary2,
                        color: colors.primary,
                        width: "150px",
                        height: "40px",
                    }}
                    value={searchCategory}
                    onChange={(e) => setSearchCategory(e.target.value)}
                    className={"px-4 py-2 rounded-l-sm"}
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