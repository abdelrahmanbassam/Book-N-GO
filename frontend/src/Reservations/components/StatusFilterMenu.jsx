import React from 'react';
import { Filter } from 'lucide-react';


export function StatusFilterMenu({selectedFilter, onFilterChange, availableFilters}) {
    return (
        <div className="flex items-center space-x-2 mb-6">
            <Filter size={20} className="text-gray-500" />
            <div className="flex flex-wrap gap-2">
                {availableFilters.map((filter) => (
                    <button
                        key={filter}
                        onClick={() => onFilterChange(filter)}
                        className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                            selectedFilter === filter
                                ? 'bg-blue-600 text-white'
                                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                        }`}
                    >
                        {filter.charAt(0).toUpperCase() + filter.slice(1)}
                    </button>
                ))}
            </div>
        </div>
    );
}