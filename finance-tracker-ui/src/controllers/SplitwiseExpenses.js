
const fetchGroups = async () => {
  try {
    const response = await axios.get('/api/groups');
    setGroups(response.data.groups);
  } catch (error) {
    console.error("Error fetching groups:", error);
  }
};

export default fetchGroups