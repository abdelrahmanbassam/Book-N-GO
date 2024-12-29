import React from 'react';

const aminities = ['Screen', 'Projector', 'AC', 'Ceiling Fans', 'White Board'];

const aminitiesFilter = ({ selected, onChange }) => {
  const handleCheckboxChange = (aminity) => {
    // Toggle the selected state for the aminity
    const newSelected = selected.includes(aminity)
      ? selected.filter((item) => item !== aminity) // Remove the aminity
      : [...selected, aminity]; // Add the aminity
    onChange(newSelected);
  };

  return (
    <div className="mb-6">
      <h3 className="text-white mb-2">Aminities:</h3>
      <div className="space-y-2">
        {aminities.map((aminity) => (
          <label key={aminity} className="flex items-center text-white">
            <input
              type="checkbox"
              className="mr-2"
              checked={selected.includes(aminity)}
              onChange={() => handleCheckboxChange(aminity)}
            />
            {aminity}
          </label>
        ))}
      </div>
    </div>
  );
};

export default aminitiesFilter;
