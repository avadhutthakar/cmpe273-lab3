package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.List;

import com.google.common.hash.Hashing;

public class Client {

	public static void main(String[] args) throws Exception {
		String[] letterArray = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
				"j" };
		String []serverArray = {"http://localhost:3000","http://localhost:3001","http://localhost:3002"};
		System.out.println("Starting Cache Client...");
		// Creating instances of distributed cache services
		CacheServiceInterface cache = new DistributedCacheService(
				serverArray[0]);
		CacheServiceInterface cache1 = new DistributedCacheService(
				serverArray[1]);
		CacheServiceInterface cache2 = new DistributedCacheService(
				serverArray[2]);
		List<CacheServiceInterface> serverDistributionList = new ArrayList<CacheServiceInterface>();

		// Add the service into the list
		serverDistributionList.add(cache);
		serverDistributionList.add(cache1);
		serverDistributionList.add(cache2);

		// Distribute the values in the services.
		int keySize = 1;
		for (; keySize <= 10; keySize++) {
			// For consistent hashing non-cryptographic hash function returned
			// by com.google.common.hash.Hashing is used
			int bucket = Hashing.consistentHash(Hashing.md5().hashInt(keySize),
					serverDistributionList.size());
			serverDistributionList.get(bucket).put(keySize,
					letterArray[(keySize - 1)]);
			System.out.println(keySize + " => " + letterArray[keySize - 1]
					+ " is assigned to server " + serverArray[bucket] + ".");
		}

		// Retrieve the values from services.
		keySize = 1;
		for (; keySize <= 10; keySize++) {
			int bucket = Hashing.consistentHash(Hashing.md5().hashInt(keySize),
					serverDistributionList.size());
			System.out.println("The value received from server is:" + keySize + "=>"
					+ serverDistributionList.get(bucket).get(keySize) + ".");
		}
		System.out.println("Existing Cache Client...");
	}
}
